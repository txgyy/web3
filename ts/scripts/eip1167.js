// We require the Hardhat Runtime Environment explicitly here. This is optional
// but useful for running the script in a standalone fashion through `node <script>`.
//
// You can also run a script with `npx hardhat run <script>`. If you do that, Hardhat
// will compile your contracts, add the Hardhat Runtime Environment's members to the
// global scope, and execute the script.
const hre = require("hardhat");

async function main() {

    const EIP1167 = await hre.ethers.getContractFactory("EIP1167");
    const eip1167 = await EIP1167.deploy("0x1cC4D981e897A3D2E7785093A648c0a75fAd0453");

    console.log(
        "address: " + eip1167.address
    );
}

// We recommend this pattern to be able to use async/await everywhere
// and properly handle errors.
main().catch((error) => {
    console.error(error);
    process.exitCode = 1;
});
