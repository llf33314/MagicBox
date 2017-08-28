package com.gt.magicbox.exchange;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/8/23 0023.
 */

public class ExchangeChooseActivity extends BaseActivity {
    String [] persons={"wer","wer","wer","hhtrhtrhtr","","","","","","","","",""};
    @BindView(R.id.choose_person)
    Button choosePerson;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_choose);
        setToolBarTitle("交班");
    }

    @OnClick({R.id.exchange_choose_ok, R.id.exchange_choose_cancel,R.id.choose_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_person:
                new AlertDialog.Builder(ExchangeChooseActivity.this).setItems(persons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choosePerson.setText(persons[which]);
                    }
                });
                break;
            case R.id.exchange_choose_ok:
                break;
            case R.id.exchange_choose_cancel:
                onBackPressed();
                break;
        }
    }
}
