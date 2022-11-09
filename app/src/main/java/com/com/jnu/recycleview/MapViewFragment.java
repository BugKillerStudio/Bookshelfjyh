package com.com.jnu.recycleview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.com.jnu.recycleview.data.BookLocation;
import com.com.jnu.recycleview.data.HttpDataSaver;

import java.util.List;

public class MapViewFragment extends Fragment {

    private MapView mapView;
    public MapViewFragment() {
    }

    public static MapViewFragment newInstance() {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView=rootView.findViewById(R.id.bmapView);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(18.0f);
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        LatLng cenpt = new LatLng(22.255925,113.541112);//中心点的坐标(坐标在坐标拾取系统里获得)
        MapStatus mMapStatus = new MapStatus.Builder()//定义地图状态
                .target(cenpt)       //要移动的点
                .zoom(18)            //放大地图到18倍
                .build();

        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

        //新线程获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpDataSaver dataLoader=new HttpDataSaver();
                String shopJsonData= dataLoader.getHttpData("http://file.nidama.net/class/mobile_develop/data/bookstore2022.json");
                List<BookLocation> locations=dataLoader.ParseJsonData(shopJsonData);

                MapViewFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AddMarkersOnMap(locations);
                    }
                });
            }
        }).start();

//        BitmapDescriptor bitmap= BitmapDescriptorFactory.fromResource(R.drawable.local);//图标型标记
//        OverlayOptions options = new MarkerOptions().position(cenpt).icon(bitmap);
//        //将maker添加到地图
//        mapView.getMap().addOverlay(options);
//        mapView.getMap().addOverlay(new TextOptions().bgColor(0xAAFFFF00)
//
//                .fontSize(24)
//
//                .fontColor(0xFFFF00FF).text("school").position(cenpt));//文字型标记

        mapView.getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapViewFragment.this.getContext(), "Marker clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return rootView;
    }

    private void AddMarkersOnMap(List<BookLocation> locations) {
        BitmapDescriptor bitmap= BitmapDescriptorFactory.fromResource(R.drawable.local);
        for (BookLocation shop: locations) {
            LatLng shopPoint = new LatLng(shop.getLatitude(),shop.getLongitude());

            OverlayOptions options = new MarkerOptions().position(shopPoint).icon(bitmap);
            //将maker添加到地图
            mapView.getMap().addOverlay(options);
            mapView.getMap().addOverlay(new TextOptions().bgColor(0xAAFFFF00)
                    .fontSize(32)
                    .fontColor(0xFFFF00FF).text(shop.getName()).position(shopPoint));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
}
