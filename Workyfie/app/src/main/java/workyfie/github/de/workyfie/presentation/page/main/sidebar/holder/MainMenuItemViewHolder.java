package workyfie.github.de.workyfie.presentation.page.main.sidebar.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.SidebarItem;

public class MainMenuItemViewHolder extends RecyclerView.ViewHolder {
    TextView textViewItem;
    ImageView imageViewItem;

    public MainMenuItemViewHolder(View itemView) {
        super(itemView);
        textViewItem = (TextView) itemView.findViewById(R.id.tv__sidebar__item);
        imageViewItem = (ImageView) itemView.findViewById(R.id.iv__sidebar__item);
    }

    public void setItem(SidebarItem item) {
        textViewItem.setText(item.resIdLabel);
        imageViewItem.setImageResource(item.resIdIcon);
    }
}
