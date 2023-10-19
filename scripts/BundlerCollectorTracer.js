{
    callsFromEntryPoint: [],
      currentLevel: null,
  keccak: [],
  calls: [],
  logs: [],
  debug: [],
  lastOp: '',
  lastThreeOpcodes: [],
  // event sent after all validations are done: keccak("BeforeExecution()")
  stopCollectingTopic: 'bb47ee3e183a558b1a2ff0874b079f3fc5478b7454eacf2bfc5af2ff5878f972',
  stopCollecting: false,
  topLevelCallCounter: 0,
  fault(log, db) {
    this.debug.push('fault depth=', log.getDepth(), ' gas=', log.getGas(), ' cost=', log.getCost(), ' err=', log.getError());
},
    result(ctx, db) {
    return {
        callsFromEntryPoint: this.callsFromEntryPoint,
        keccak: this.keccak,
        logs: this.logs,
        calls: this.calls,
        debug: this.debug // for internal debugging.
    };
},
    enter(frame) {
    if (this.stopCollecting) {
        return;
    }
    // this.debug.push('enter gas=', frame.getGas(), ' type=', frame.getType(), ' to=', toHex(frame.getTo()), ' in=', toHex(frame.getInput()).slice(0, 500))
    this.calls.push({
        type: frame.getType(),
        from: toHex(frame.getFrom()),
        to: toHex(frame.getTo()),
        method: toHex(frame.getInput()).slice(0, 10),
        gas: frame.getGas(),
        value: frame.getValue()
    });
},
    exit(frame) {
    if (this.stopCollecting) {
        return;
    }
    this.calls.push({
        type: frame.getError() != null ? 'REVERT' : 'RETURN',
        gasUsed: frame.getGasUsed(),
        data: toHex(frame.getOutput()).slice(0, 4000)
    });
},
    // increment the "key" in the list. if the key is not defined yet, then set it to "1"
    countSlot(list, key) {
    var _a;
    list[key] = ((_a = list[key]) !== null && _a !== void 0 ? _a : 0) + 1;
},
    step(log, db) {
    var _a;
    if (this.stopCollecting) {
        return;
    }
    opcode = log.op.toString();
    stackSize = log.stack.length();
    stackTop3 = [];
    for ( i = 0; i < 3 && i < stackSize; i++) {
        stackTop3.push(log.stack.peek(i));
    }
    this.lastThreeOpcodes.push({ opcode, stackTop3 });
    if (this.lastThreeOpcodes.length > 3) {
        this.lastThreeOpcodes.shift();
    }
    // this.debug.push(this.lastOp + '-' + opcode + '-' + log.getDepth() + '-' + log.getGas() + '-' + log.getCost())
    if (log.getGas() < log.getCost() || (
      // special rule for SSTORE with gas metering
      opcode === 'SSTORE' && log.getGas() < 2300)) {
        this.currentLevel.oog = true;
    }
    if (opcode === 'REVERT' || opcode === 'RETURN') {
        if (log.getDepth() === 1) {
            // exit() is not called on top-level return/revent, so we reconstruct it
            // from opcode
            ofs = parseInt(log.stack.peek(0).toString());
            len = parseInt(log.stack.peek(1).toString());
            data = toHex(log.memory.slice(ofs, ofs + len)).slice(0, 4000);
            // this.debug.push(opcode + ' ' + data)
            this.calls.push({
                type: opcode,
                gasUsed: 0,
                data
            });
        }
        // NOTE: flushing all history after RETURN
        this.lastThreeOpcodes = [];
    }
    if (log.getDepth() === 1) {
        if (opcode === 'CALL' || opcode === 'STATICCALL') {
            // stack.peek(0) - gas
            addr = toAddress(log.stack.peek(1).toString(16));
            topLevelTargetAddress = toHex(addr);
            // stack.peek(2) - value
            ofs = parseInt(log.stack.peek(3).toString());
            // stack.peek(4) - len
            topLevelMethodSig = toHex(log.memory.slice(ofs, ofs + 4));
            this.currentLevel = this.callsFromEntryPoint[this.topLevelCallCounter] = {
                topLevelMethodSig,
                topLevelTargetAddress,
                access: {},
                opcodes: {},
                extCodeAccessInfo: {},
                contractSize: {}
            };
            this.topLevelCallCounter++;
        }
        else if (opcode === 'LOG1') {
            // ignore log data ofs, len
            topic = log.stack.peek(2).toString(16);
            if (topic === this.stopCollectingTopic) {
                this.stopCollecting = true;
            }
        }
        this.lastOp = '';
        return;
    }
    lastOpInfo = this.lastThreeOpcodes[this.lastThreeOpcodes.length - 2];
    // store all addresses touched by EXTCODE* opcodes
    if (((_a = lastOpInfo === null || lastOpInfo === void 0 ? void 0 : lastOpInfo.opcode) === null || _a === void 0 ? void 0 : _a.match(/^(EXT.*)$/)) != null) {
        addr = toAddress(lastOpInfo.stackTop3[0].toString(16));
        addrHex = toHex(addr);
        last3opcodesString = this.lastThreeOpcodes.map(x => x.opcode).join(' ');
        // only store the last EXTCODE* opcode per address - could even be a boolean for our current use-case
        // [OP-051]
        if (last3opcodesString.match(/^(\w+) EXTCODESIZE ISZERO$/) == null) {
            this.currentLevel.extCodeAccessInfo[addrHex] = opcode;
            // this.debug.push(`potentially illegal EXTCODESIZE without ISZERO for ${addrHex}`)
        }
        else {
            // this.debug.push(`safe EXTCODESIZE with ISZERO for ${addrHex}`)
        }
    }
    // not using 'isPrecompiled' to only allow the ones defined by the ERC-4337 as stateless precompiles
    // [OP-062]
    isAllowedPrecompiled = (address) => {
        addrHex = toHex(address);
        addressInt = parseInt(addrHex);
        // this.debug.push(`isPrecompiled address=${addrHex} addressInt=${addressInt}`)
        return addressInt > 0 && addressInt < 10;
    };
    // [OP-041]
    if (opcode.match(/^(EXT.*|CALL|CALLCODE|DELEGATECALL|STATICCALL)$/) != null) {
        idx = opcode.startsWith('EXT') ? 0 : 1;
        addr = toAddress(log.stack.peek(idx).toString(16));
        addrHex = toHex(addr);
        // this.debug.push('op=' + opcode + ' last=' + this.lastOp + ' stacksize=' + log.stack.length() + ' addr=' + addrHex)
        if (this.currentLevel.contractSize[addrHex] == null && !isAllowedPrecompiled(addr)) {
            this.currentLevel.contractSize[addrHex] = {
                contractSize: db.getCode(addr).length,
                opcode
            };
        }
    }
    // [OP-012]
    if (this.lastOp === 'GAS' && !opcode.includes('CALL')) {
        // count "GAS" opcode only if not followed by "CALL"
        this.countSlot(this.currentLevel.opcodes, 'GAS');
    }
    if (opcode !== 'GAS') {
        // ignore "unimportant" opcodes:
        if (opcode.match(/^(DUP\d+|PUSH\d+|SWAP\d+|POP|ADD|SUB|MUL|DIV|EQ|LTE?|S?GTE?|SLT|SH[LR]|AND|OR|NOT|ISZERO)$/) == null) {
            this.countSlot(this.currentLevel.opcodes, opcode);
        }
    }
    this.lastOp = opcode;
    if (opcode === 'SLOAD' || opcode === 'SSTORE') {
        slot = toWord(log.stack.peek(0).toString(16));
        slotHex = toHex(slot);
        addr = log.contract.getAddress();
        addrHex = toHex(addr);
        access = this.currentLevel.access[addrHex];
        if (access == null) {
            access = {
                reads: {},
                writes: {}
            };
            this.currentLevel.access[addrHex] = access;
        }
        if (opcode === 'SLOAD') {
            // read slot values before this UserOp was created
            // (so saving it if it was written before the first read)
            if (access.reads[slotHex] == null && access.writes[slotHex] == null) {
                access.reads[slotHex] = toHex(db.getState(addr, slot));
            }
        }
        else {
            this.countSlot(access.writes, slotHex);
        }
    }
    if (opcode === 'KECCAK256') {
        // collect keccak on 64-byte blocks
        ofs = parseInt(log.stack.peek(0).toString());
        len = parseInt(log.stack.peek(1).toString());
        // currently, solidity uses only 2-word (6-byte) for a key. this might change..
        // still, no need to return too much
        if (len > 20 && len < 512) {
            // if (len === 64) {
            this.keccak.push(toHex(log.memory.slice(ofs, ofs + len)));
        }
    }
    else if (opcode.startsWith('LOG')) {
        count = parseInt(opcode.substring(3));
        ofs = parseInt(log.stack.peek(0).toString());
        len = parseInt(log.stack.peek(1).toString());
        topics = [];
        for ( i = 0; i < count; i++) {
            // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
            topics.push('0x' + log.stack.peek(2 + i).toString(16));
        }
        data = toHex(log.memory.slice(ofs, ofs + len));
        this.logs.push({
            topics,
            data
        });
    }
}
}
