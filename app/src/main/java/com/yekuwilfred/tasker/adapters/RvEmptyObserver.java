package com.yekuwilfred.tasker.adapters;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RvEmptyObserver extends RecyclerView.AdapterDataObserver {
        private View emptyView;
        private RecyclerView recyclerView;

        public RvEmptyObserver(RecyclerView rv, View emptyView) {
            this.recyclerView = rv;
            this.emptyView    = emptyView;
            checkIfEmpty();
        }

        private void checkIfEmpty() {
            if (emptyView != null && recyclerView.getAdapter() != null) {
                boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
                emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
            }
        }

        public void onChanged() { checkIfEmpty(); }
        public void onItemRangeInserted(int positionStart, int itemCount) { checkIfEmpty(); }
        public void onItemRangeRemoved(int positionStart, int itemCount) { checkIfEmpty(); }
}
