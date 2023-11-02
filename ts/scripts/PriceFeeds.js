let {ethers} = require("hardhat");

async function main() {
    const address = "0x7b9DE8E940003801f23aBE26c80f863b9Da151b2"
    await test(address)
}

async function test(address) {
    const OpL1Block = await ethers.getContractFactory("OpL1Block");
    const opL1Block = await OpL1Block.attach(address)
    const price = await opL1Block.getAllInfo()
    console.log(price)
}


main()
    .then(() => process.exit(0))
    .catch((error) => {
        console.error(error);
        process.exit(1);
    });
