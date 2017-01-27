package workyfie.github.de.workyfie.presentation.page.main.sidebar;

import workyfie.github.de.workyfie.R;

public enum SidebarItem {
    MEASURE(R.drawable.dna, R.string.sidebar_measure,  R.string.app_name),
    HISTORY(R.drawable.book_open_varaint, R.string.sidebar_history, R.string.sidebar_history),
    SENSOR(R.drawable.bluetooth_setting, R.string.sidebar_sensor, R.string.sidebar_sensor),
    INFORMATION(R.drawable.information_variant, R.string.information, R.string.information);

    public final int resIdIcon;
    public final int resIdLabel;
    public final int resTitle;

    SidebarItem(int resIdIcon, int resIdLabel, int resTitle) {
        this.resIdIcon = resIdIcon;
        this.resIdLabel = resIdLabel;
        this.resTitle = resTitle;
    }
}
