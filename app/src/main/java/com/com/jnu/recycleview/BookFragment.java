package com.com.jnu.recycleview;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.com.jnu.recycleview.data.Book;
import com.com.jnu.recycleview.data.DataSaver;

import java.util.ArrayList;

public class BookFragment extends Fragment {
    private static final int MENU_ID_ADD =1;
    private static final int MENU_ID_UPDATE =2;
    private static final int MENU_ID_DELETE =3;
    public ArrayList<Book> books;//Book列表
    private BookFragment.MainRecycleViewAdapter mainRecycleViewAdapter;

    private final ActivityResultLauncher<Intent> addDataLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result->{
                if(null!=result){
                    Intent intent=result.getData();//获得传回的intent
                    if(result.getResultCode()== EditBookActivity.RESULT_CODE_SUCCESS)
                    {
                        assert intent != null;
                        Bundle bundle=intent.getExtras();
                        String title=bundle.getString("title");//获得传回的新的title
                        int position=bundle.getInt("position");//获得传回的当前数据的位置
                        books.add(position, new Book(title,R.drawable.book_no_name));//添加一个新的Book
                        new DataSaver().Save(this.getContext(),books);//数据保存
                        mainRecycleViewAdapter.notifyItemInserted(position);//通知适配器数据增加
                    }
                }
            } );//新的数据传递（add的数据回传）
    private final ActivityResultLauncher<Intent> updateDataLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result->{
                if(null!=result){
                    Intent intent=result.getData();//获得传回的intent
                    if(result.getResultCode()== EditBookActivity.RESULT_CODE_SUCCESS)
                    {
                        assert intent != null;
                        Bundle bundle=intent.getExtras();
                        String title=bundle.getString("title");//获得传回的新的title
                        int position=bundle.getInt("position");//获得传回的当前数据的位置
                        books.get(position).setTitle(title);//修改title为传回的新的title
                        new DataSaver().Save(this.getContext(),books);//数据保存
                        mainRecycleViewAdapter.notifyItemChanged(position);//通知适配器数据更改
                    }
                }
            } );//新的数据传递（update的数据回传）

    public BookFragment() {
        // Required empty public constructor
    }

    public static BookFragment newInstance() {
        BookFragment fragment = new BookFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_book, container, false);//布局解析器
        RecyclerView recyclerViewMain = rootView.findViewById(R.id.recycle_view_books);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//垂直
        recyclerViewMain.setLayoutManager(linearLayoutManager);

        DataSaver dataSaver=new DataSaver();
        books=dataSaver.Load(this.getContext());

        if(books.size()==0) {
            for (int i = 1; i < 2; ++i) {
                Book a = new Book("软件项目管理案例教程（第4版）", R.drawable.book_2);
                books.add(a);
                Book b = new Book("创新工程实践", R.drawable.book_no_name);
                books.add(b);
                Book c = new Book("信息安全数学基础（第2版）", R.drawable.book_1);
                books.add(c);
            }
        }
        mainRecycleViewAdapter = new MainRecycleViewAdapter(books);
        recyclerViewMain.setAdapter(mainRecycleViewAdapter);
        return rootView;
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case MENU_ID_ADD:
                Intent intent=new Intent(this.getContext(), EditBookActivity.class);
                intent.putExtra("position",item.getOrder());//传递当前位置
                addDataLauncher.launch(intent);//数据回传
                break;
            case MENU_ID_UPDATE:
                Intent intentupdate=new Intent(this.getContext(), EditBookActivity.class);
                intentupdate.putExtra("position",item.getOrder());//传递当前位置
                intentupdate.putExtra("title",books.get(item.getOrder()).getTitle());//传递当前的title
                updateDataLauncher.launch(intentupdate);//数据回传
                break;
            case MENU_ID_DELETE:
                AlertDialog alertDialog=new AlertDialog.Builder(this.getContext())
                        .setTitle(R.string.confirmation)
                        .setMessage(R.string.sure_to_delete)
                        .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                            books.remove(item.getOrder());
                            new DataSaver().Save(BookFragment.this.getContext(),books);//数据保存
                            mainRecycleViewAdapter.notifyItemRemoved(item.getOrder());
                        }).setNegativeButton(R.string.no, (dialog, which) -> {
                        }).create();
                alertDialog.show();//对话框
                break;
        }
        return super.onContextItemSelected(item);
    }

    public  ArrayList<Book> getListBooks(){
        return books;
    }

    public static class MainRecycleViewAdapter extends RecyclerView.Adapter<MainRecycleViewAdapter.ViewHolder> {

        private final ArrayList<Book> localDataSet;

        public static final class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textView;
            private final ImageView imageview;

            public ViewHolder(View view) {
                super(view);
                textView = view.findViewById(R.id.text_view_book_title);
                imageview = view.findViewById(R.id.image_view_book_cover);

                view.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0,MENU_ID_ADD,getAdapterPosition(),"Add "+getAdapterPosition());
                contextMenu.add(0,MENU_ID_UPDATE,getAdapterPosition(),"Update "+getAdapterPosition());
                contextMenu.add(0,MENU_ID_DELETE,getAdapterPosition(),"Delete "+getAdapterPosition());
            }//上下文菜单的设置
        }

        public MainRecycleViewAdapter(ArrayList<Book> dataSet) {
            localDataSet = dataSet;
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_list_main, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Book book = localDataSet.get(position);
            viewHolder.imageview.setImageResource(book.getCoverResourceId());
            viewHolder.textView.setText(book.getTitle());
        }

        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }
}