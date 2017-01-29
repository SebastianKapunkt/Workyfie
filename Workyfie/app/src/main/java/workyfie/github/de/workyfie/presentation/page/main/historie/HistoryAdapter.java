package workyfie.github.de.workyfie.presentation.page.main.historie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.subjects.PublishSubject;
import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.data.view.models.Session;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Session> items;
    private final PublishSubject<String> onClickHistoryItem = PublishSubject.create();

    public HistoryAdapter(List<Session> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.history_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(String.format("%s", items.get(position).name));
        LocalDateTime startDateTime = LocalDateTime.ofInstant(items.get(position).startTime, ZoneId.systemDefault());
        holder.date.setText(String.format(
                "%s %s %s",
                startDateTime.getDayOfMonth(),
                startDateTime.getMonth(),
                startDateTime.getYear())
        );
        holder.duration.setText(String.format(
                "%s h %s min %s sek",
                Duration.between(items.get(position).startTime, items.get(position).endTime).toHours(),
                Duration.between(items.get(position).startTime, items.get(position).endTime).toMinutes(),
                Duration.between(items.get(position).startTime, items.get(position).endTime).toMillis() / 1000)
        );
        holder.itemView.setOnClickListener(v -> onClickHistoryItem.onNext(items.get(position).id));
    }

    public Observable<String> getHistoryClicks() {
        return onClickHistoryItem;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView date;
        public TextView duration;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name_field);
            date = (TextView) v.findViewById(R.id.date_field);
            duration = (TextView) v.findViewById(R.id.duration_field);
        }
    }
}
