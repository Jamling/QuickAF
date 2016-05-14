package cn.ieclipse.af.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * GridAdapter used for similar grid layout e.g.{@link cn.ieclipse.af.view.TableLayout}
 * @param <T>
 */
public abstract class AfGridAdapter<T, E> extends AfBaseAdapter<T> {
    private int skipColumns = 0;

    @Override
    public int getCount() {
        return getRowCount() * (getColumnCount() - getSkipColumns());
    }

    @Override
    public T getItem(int position) {
        // throw new UnsupportedOperationException("Unsupported method, please use getItem(row, col) instead!");
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int row = position / (getColumnCount() - getSkipColumns());
        int col = position % (getColumnCount() - getSkipColumns()) + getSkipColumns();
        return getView(row, col, convertView, parent);
    }

    public abstract int getRowCount();

    public abstract int getColumnCount();

    public abstract void onUpdateView(View convertView, int row, int column);

    @Override
    public void onUpdateView(View convertView, int position) {
        throw new UnsupportedOperationException("Unsupported method, please use onUpdateView(view, row, col) instead!");
    }

    public View getView(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null && getLayout() > 0) {
            // convertView = View.inflate(parent.getContext(), getLayout(), parent);
            convertView = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        }
        try {
            onUpdateView(convertView, row, column);
        } catch (Exception e) {
            mLogger.e("exception onUpdateView", e);
        }
        return convertView;
    }

    public abstract E getItem(int row, int col);

    public int getSkipColumns() {
        return skipColumns;
    }

    public void setSkipColumns(int skipColumns) {
        if (skipColumns >= 0) {
            this.skipColumns = skipColumns;
            notifyDataSetChanged();
        }
    }
}
