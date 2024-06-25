package com.example.p2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.p2.R;
import com.example.p2.dao.EnderecoDao;
import com.example.p2.database.AppDatabase;
import com.example.p2.databinding.ActivityMapaEnderecoBinding;
import com.example.p2.entities.Endereco;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaEndereco extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapaEndereco";

    private GoogleMap mapeamento ;
    private ActivityMapaEnderecoBinding binding;
    private double lati, longi;
    private String endMarcado;
    private int enderecoId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapaEnderecoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize location data
        LatLng latLng = obterLocalizacao();
        if (latLng != null) {
            lati = latLng.latitude;
            longi = latLng.longitude;
            if(lati>50){
                mark = false;
            }else{mark = true;
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null && mark == true) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Erro ao carregar o mapa", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
  public void onMapReady(GoogleMap googleMap) {
        mapeamento = googleMap;
        sharedPreferences = getSharedPreferences("locPref", MODE_PRIVATE);
        endMarcado = sharedPreferences.getString("endMarcado", "endereço não encontrado.");
        LatLng latLng = new LatLng(lati, longi);
        mapeamento .addMarker(new MarkerOptions().position(latLng).title(endMarcado));
        mapeamento .moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mapeamento .setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapeamento .animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    private void setupMapTypeButtons() {
        Button buttonNormal = findViewById(R.id.buttonNormal);
        Button buttonSatellite = findViewById(R.id.buttonSatellite);
        Button buttonTerrain = findViewById(R.id.buttonTerrain);
        Button buttonHybrid = findViewById(R.id.buttonHybrid);

        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });

        buttonSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });

        buttonTerrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
            }
        });

        buttonHybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
            }
        });
    }

    private LatLng obterLocalizacao() {
        sharedPreferences = getSharedPreferences("locPref", MODE_PRIVATE);
        enderecoId = sharedPreferences.getInt("idEnd", -1);

        // Log the retrieved enderecoId
        Log.d(TAG, "Retrieved enderecoId: " + enderecoId);

        EnderecoDao enderecoDao = AppDatabase.getDatabase(this).enderecoDao();
        Endereco endereco = enderecoDao.getEndereco(enderecoId);

        // Debug print
        Log.d(TAG, "Endereco: " + endereco);

        if (endereco != null) {
            longi = endereco.getLongitude();
            lati = endereco.getLatitude();
            endMarcado = endereco.getDescricao();

            // Debug prints for latitude and longitude
            Log.d(TAG, "Longitude: " + longi);
            Log.d(TAG, "Latitude: " + lati);

            return new LatLng(lati, longi);
        } else {
            Toast.makeText(this, "Erro: endereço não encontrado", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
