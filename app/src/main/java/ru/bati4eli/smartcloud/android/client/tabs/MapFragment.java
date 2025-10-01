package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;

public class MapFragment extends Fragment implements OnMapReadyCallback, OnBackPressedListener {

    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;

        // Базовые настройки
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        // Пример: поставить маркер и переместить камеру
        LatLng moscow = new LatLng(55.751244, 37.618423);
        googleMap.addMarker(new MarkerOptions().position(moscow).title("Москва"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moscow, 10f));
    }

    @Override
    public void onBackPressed(Runnable superOnBackPressed) {
        // Если хотите обработать back внутри карты/фрагмента — добавьте логику тут.
        // По умолчанию пробрасываем назад.
        if (superOnBackPressed != null) superOnBackPressed.run();
    }
}
