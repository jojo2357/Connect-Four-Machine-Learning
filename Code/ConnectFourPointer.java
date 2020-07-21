public class ConnectFourPointer {

    private short fcID;
    private short rcID;
    private int myIndex;

    public ConnectFourPointer(int index, int fcID, int lcID){
        this.myIndex = index;
        this.fcID = (short)fcID;
        this.rcID = (short)lcID;
    }

    public int getID(){
        return this.fcID;
    }

    public int getMyIndex(){
        return this.myIndex;
    }

    public int getRID(){
        return this.rcID;
    }
}