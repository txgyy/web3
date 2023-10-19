package xin.yukino.web3.util.event;

public interface IReEnter {

    long getLastBlockNumber();

    int getLastLogIndex();


    void setLastBlockNumber(long lastBlockNumber);

    void setLastLogIndex(int lastLogIndex);

    default boolean hasEntered(IIndex iIndex) {
        if (getLastBlockNumber() < iIndex.getBlockNumber()) {
            return false;
        }

        if (getLastBlockNumber() > iIndex.getBlockNumber()) {
            return true;
        }

        return getLastLogIndex() >= iIndex.getLogIndex();
    }

    default void updateIndex(IIndex iIndex) {
        setLastBlockNumber(iIndex.getBlockNumber());
        setLastLogIndex(iIndex.getLogIndex());
    }


}
