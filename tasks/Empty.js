task("numberMarker", "numberMarker")
    .setAction(async (taskArgs, { ethers }) => {
        const Empty = await ethers.getContractFactory("Empty");
        const accounts = await ethers.getSigners();
        const signer = accounts[0];
        const contractAddress = "0x06b5bD1506c503eA2fAA48b761B250A481B1789b";
        const testContract = await new ethers.Contract(
            contractAddress,
            Empty.interface,
            signer
        );

        let result = await testContract.numberMarker();

        console.log(result);
    });