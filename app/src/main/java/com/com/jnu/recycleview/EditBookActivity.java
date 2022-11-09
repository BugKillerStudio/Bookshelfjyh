package com.com.jnu.recycleview;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBookActivity extends AppCompatActivity {

    public static final int RESULT_CODE_SUCCESS = 666;
    private int position;//传入与传出当前位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book_activity);

        position=this.getIntent().getIntExtra("position",0);//传入当前位置
        EditText editTextTitle=findViewById(R.id.edittext_shop_item_title);

        String title=this.getIntent().getStringExtra("title");//传入当前title
        if(null!=title){
            editTextTitle.setText(title);//将EditText的值修改为传过来的title
        }//title为空是增加操作，不为空是更新操作

        Button button_yes=findViewById(R.id.button_确定);
        Button button_no=findViewById(R.id.button_取消);

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("title", editTextTitle.getText().toString());//传回新输入的title
                bundle.putInt("position",position);//传回当前位置

                intent.putExtras(bundle);
                setResult(RESULT_CODE_SUCCESS, intent);//结果码
                EditBookActivity.this.finish();//记得关闭当前的activity
            }
        });
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditBookActivity.this.finish();//选择取消按钮退出EditBookActivity
            }
        });
    }
}