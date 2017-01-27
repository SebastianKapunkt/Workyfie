package workyfie.github.de.workyfie.presentation.page.main.historie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        holder.id.setText(String.format("id: %s", items.get(position).id));
        holder.name.setText(String.format("name: %s", items.get(position).name));
        if (items.get(position).startTime != null) {
            holder.startTime.setText(String.format("startTime: %s", formatter.format(items.get(position).startTime)));
        }
        if (items.get(position).endTime != null) {
            holder.endTime.setText(String.format("endTime: %s", formatter.format(items.get(position).endTime)));
        }
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
        public TextView id;
        public TextView name;
        public TextView startTime;
        public TextView endTime;

        public ViewHolder(View v) {
            super(v);
            id = (TextView) v.findViewById(R.id.id_field);
            name = (TextView) v.findViewById(R.id.name_field);
            startTime = (TextView) v.findViewById(R.id.start_time_field);
            endTime = (TextView) v.findViewById(R.id.end_time_field);
        }
    }
}
