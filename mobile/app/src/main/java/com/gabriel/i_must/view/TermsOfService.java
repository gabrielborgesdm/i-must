package com.gabriel.i_must.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.gabriel.i_must.R;

import java.util.Locale;

public class TermsOfService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);
        WebView web = (WebView) findViewById(R.id.webview_terms_of_service);
        if(Locale.getDefault().toLanguageTag().equals("pt-BR")){
            web.loadUrl("file:///android_asset/terms-and-conditions-pt.html");
        } else {
            web.loadUrl("file:///android_asset/terms-and-conditions.html");

        }

    }

}