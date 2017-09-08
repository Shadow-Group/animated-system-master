package com.osama.project34.ui.widgets;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


/**
 * Created by bullhead on 9/8/17.
 *
 */

public class EmailViewer extends WebViewClient {
    private ProgressBar loadingBar;

    public EmailViewer(ProgressBar loadingBar) {
        this.loadingBar = loadingBar;
        loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        loadingBar.setVisibility(View.GONE);
    }
}
