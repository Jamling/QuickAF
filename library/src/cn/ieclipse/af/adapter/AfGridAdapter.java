package cn.ieclipse.af.adapter;


public abstract class AfGridAdapter<T> extends AfBaseAdapter<T> {
    private int mNumColumns = 1;
    private int mNumRows = 1;
    
    public void setColumns(int columns) {
        this.mNumColumns = columns;
    }
    
    public void setRows(int rows) {
        this.mNumRows = rows;
    }
    
    @Override
    public int getCount() {
        int count = super.getCount();
        return Math.min(count, mNumRows * mNumColumns);
    }
}
