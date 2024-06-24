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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaEndereco extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapaEndereco";

    private GoogleMap map;
    private ActivityMapaEnderecoBinding binding;
    private double lati, longi;
    private String endMarcado;
    private int enderecoId;
    private SharedPreferences sharedPreferences;
    private Button voltar;
    private Button edtEnd;

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
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Erro ao carregar o mapa", Toast.LENGTH_SHORT).show();
        }
        voltar = findViewById(R.id.btn_voltarEndereco);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaEndereco.this, GerenciarEndereco.class);
                startActivity(intent);
            }
        });
        edtEnd = findViewById(R.id.edtEndMapa);
        edtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaEndereco.this, EdtEndereco.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        sharedPreferences = getSharedPreferences("locPref", MODE_PRIVATE);
        endMarcado = sharedPreferences.getString("endMarcado", "endereço não encontrado.");
        LatLng latLng = new LatLng(lati, longi);
        map.addMarker(new MarkerOptions().position(latLng).title(endMarcado));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
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
            print("Mapa não encontrado");
            print("Mapa encontrado");
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
