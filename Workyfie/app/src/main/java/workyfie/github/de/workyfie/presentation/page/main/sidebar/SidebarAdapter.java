package workyfie.github.de.workyfie.presentation.page.main.sidebar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.holder.DividerViewHolder;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.holder.HeaderViewHolder;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.holder.MainMenuItemViewHolder;

public class SidebarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int MAIN_MENU_ITEM_TYPE = 1;
    private static final int DIVIDER_TYPE = 3;
    private static final int DIVIDER_POSITION_ONE = 4;

    private List<SidebarItem> sidebarItems;
    private SidebarItem selectedItem;
    private View selectedView;

    private static List<Integer> dividerPositionMain = Collections.singletonList(DIVIDER_POSITION_ONE);
    private Callback callback;

    public SidebarAdapter(List<SidebarItem> sidebarItems, SidebarItem selectedItem) {
        this.sidebarItems = sidebarItems;
        this.selectedItem = selectedItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case DIVIDER_TYPE:
                return new DividerViewHolder(inflater.inflate(R.layout.item_divider, parent, false));
            case TYPE_HEADER:
                return new HeaderViewHolder(inflater.inflate(R.layout.sidebar_header, parent, false));
            case MAIN_MENU_ITEM_TYPE:
                return new MainMenuItemViewHolder(inflater.inflate(R.layout.item_row_sidebar, parent, false));
            default:
                throw new IllegalStateException("The viewtype with id " + viewType + " is not support in sidebar menu");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case DIVIDER_TYPE:
                return;
            case MAIN_MENU_ITEM_TYPE:
                MainMenuItemViewHolder itemViewHolder = (MainMenuItemViewHolder) holder;
                final SidebarItem item = getSidebarItem(position);
                itemViewHolder.setItem(item);

                if (selectedItem == item) {
                    selectedView = itemViewHolder.itemView;
                    selectedView.setSelected(true);
                }
                itemViewHolder.itemView.setOnClickListener(v -> {
                    if (callback != null) {
                        if (selectedView != null) {
                            selectedView.setSelected(false);
                        }

                        selectedView = v;
                        v.setSelected(true);
                        selectedItem = item;
                        callback.onItemClicked(item);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 1 + (sidebarItems != null ? sidebarItems.size() + dividerPositionMain.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }

        if (dividerPositionMain.contains(position)) {
            return DIVIDER_TYPE;
        }

        return MAIN_MENU_ITEM_TYPE;
    }

    public SidebarItem getSidebarItem(int adapterPosition) {
        if (adapterPosition > 0 && adapterPosition < DIVIDER_POSITION_ONE) {
            return sidebarItems.get(adapterPosition - 1);
        } else if (adapterPosition > DIVIDER_POSITION_ONE) {
            return sidebarItems.get(adapterPosition - 2);
        } else {
            return sidebarItems.get(adapterPosition);
        }
    }

    public SidebarItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(SidebarItem selectedItem) {
        selectedView.setSelected(false);
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public void setCallback(SidebarFragment callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onItemClicked(SidebarItem item);
    }
}
