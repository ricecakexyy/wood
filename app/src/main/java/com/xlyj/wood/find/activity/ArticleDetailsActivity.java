package com.xlyj.wood.find.activity;

import android.os.Bundle;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Article;
import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.richeditor.RichEditor;

public class ArticleDetailsActivity extends BaseActivity {

    @BindView(R.id.editor)
    RichEditor editor;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_article_details);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        getDate();
    }

    /**
     * 得到数据
     */
    void getDate(){
        article = (Article) getIntent().getSerializableExtra("article");
        editor.setHtml(article.getHtml());
        editor.setEnabled(false);
    }
}
