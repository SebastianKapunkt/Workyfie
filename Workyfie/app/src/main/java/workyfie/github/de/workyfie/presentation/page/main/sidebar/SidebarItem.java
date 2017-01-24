package workyfie.github.de.workyfie.presentation.page.main.sidebar;

import workyfie.github.de.workyfie.R;

public enum SidebarItem {
    MEASURE(R.drawable.dna, R.string.sidebar_measure),
    HISTORY(R.drawable.book_open_varaint, R.string.sidebar_history),
    SENSOR(R.drawable.bluetooth_transfer, R.string.sidebar_sensor),
    INFORMATION(R.drawable.information_variant, R.string.information);

    public final int resIdIcon;
    public final int resIdLabel;

    SidebarItem(int resIdIcon, int resIdLabel) {
        this.resIdIcon = resIdIcon;
        this.resIdLabel = resIdLabel;
    }
}
