package android.eservices.rendu.presentation.moviedisplay.seen.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.eservices.rendu.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.eservices.rendu.MovieApplication.displayChangeAction;

public class SeenFragment extends Fragment {

    public static final String TAB_NAME = "Films vus";
    private View rootView;
    private RecyclerView recyclerView;

    BroadcastReceiver receiverUpdateDownload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setupLayoutOnRecyclerView((int) intent.getExtras().get(displayChangeAction));
        }
    };

    private SeenFragment() {
    }

    public static SeenFragment newInstance() {
        return new SeenFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_seen, container, false);
        IntentFilter filter = new IntentFilter(displayChangeAction);
        getActivity().registerReceiver(receiverUpdateDownload, filter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupAdapterOnRecyclerView();
        setupLayoutOnRecyclerView(1);
    }

    
    private void setupAdapterOnRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        //TODO: set adapter
    }

    private void setupLayoutOnRecyclerView(int layout) {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        if(layout == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (receiverUpdateDownload != null) {
            try {
                getActivity().unregisterReceiver(receiverUpdateDownload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
