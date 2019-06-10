package com.suntiago.baseui.ldc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.suntiago.baseui.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wangshuofeng on 2018/6/1.
 */

public class LoadIndicatorFrame extends FrameLayout {

    private static final String TAG_LOADING = "IND_LOADING";
    private static final String TAG_EMPTY = "IND_EMPTY";
    private static final String TAG_ERROR = "IND_ERROR";

    final String CONTENT = "type_content";
    final String LOADING = "type_loading";
    final String EMPTY = "type_empty";
    final String ERROR = "type_error";

    LayoutInflater inflater;
    View view;
    LayoutParams layoutParams;
    Drawable currentBackground;

    List<View> contentViews = new ArrayList<>();

    View loadingStateRelativeLayout;
    ProgressBar loadingStateProgressBar;
    boolean useDefaultLoadingStateLayout = true;

    View emptyStateRelativeLayout;
    ImageView emptyStateImageView;
    TextView emptyStateTitleTextView;
    TextView emptyStateContentTextView;
    boolean useDefaultEmptyStateLayout = true;

    View errorStateRelativeLayout;
    ImageView errorStateImageView;
    TextView errorStateTitleTextView;
    TextView errorStateContentTextView;
    Button errorStateButton;
    boolean useDefaultErrorStateLayout = true;

    int loadingStateProgressBarWidth;
    int loadingStateProgressBarHeight;
    int loadingStateBackgroundColor;

    int emptyStateImageWidth;
    int emptyStateImageHeight;
    int emptyStateTitleTextSize;
    int emptyStateContentTextSize;
    int emptyStateTitleTextColor;
    int emptyStateContentTextColor;
    int emptyStateBackgroundColor;

    int errorStateImageWidth;
    int errorStateImageHeight;
    int errorStateTitleTextSize;
    int errorStateContentTextSize;
    int errorStateTitleTextColor;
    int errorStateContentTextColor;
    int errorStateButtonTextColor;
    int errorStateBackgroundColor;

    private String state = CONTENT;
    private ImageView errorStateClickImageView;

    public LoadIndicatorFrame(Context context) {
        super(context);
    }

    public LoadIndicatorFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadIndicatorFrame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable
                .LoadIndicatorFrame);

        //Loading state attrs
        loadingStateProgressBarWidth =
                typedArray.getDimensionPixelSize(R.styleable
                        .LoadIndicatorFrame_loadingProgressBarWidth, 110);

        loadingStateProgressBarHeight =
                typedArray.getDimensionPixelSize(R.styleable
                        .LoadIndicatorFrame_loadingProgressBarHeight, 170);

        loadingStateBackgroundColor =
                typedArray.getColor(R.styleable
                        .LoadIndicatorFrame_loadingBackgroundColor, Color.TRANSPARENT);

        //Empty state attrs
        emptyStateImageWidth =
                typedArray.getDimensionPixelSize(R.styleable
                        .LoadIndicatorFrame_emptyImageWidth, 310);

        emptyStateImageHeight =
                typedArray.getDimensionPixelSize(R.styleable
                        .LoadIndicatorFrame_emptyImageHeight, 370);

        emptyStateTitleTextSize =
                typedArray.getDimensionPixelSize(R.styleable
                        .LoadIndicatorFrame_emptyTitleTextSize, 15);

        emptyStateContentTextSize =
                typedArray.getDimensionPixelSize(R.styleable
                        .LoadIndicatorFrame_emptyContentTextSize, 14);

        emptyStateTitleTextColor =
                typedArray.getColor(R.styleable.LoadIndicatorFrame_emptyTitleTextColor,
                        Color.BLACK);

        emptyStateContentTextColor =
                typedArray.getColor(R.styleable
                        .LoadIndicatorFrame_emptyContentTextColor, Color.BLACK);

        emptyStateBackgroundColor =
                typedArray.getColor(R.styleable
                        .LoadIndicatorFrame_emptyBackgroundColor, Color.TRANSPARENT);

        //Error state attrs
        errorStateImageWidth =
                typedArray.getDimensionPixelSize(R.styleable
                        .LoadIndicatorFrame_errorImageWidth, 308);

        errorStateImageHeight =
                typedArray.getDimensionPixelSize(R.styleable
                        .LoadIndicatorFrame_errorImageHeight, 308);

        errorStateTitleTextSize =
                typedArray.getDimensionPixelSize(R.styleable
                        .LoadIndicatorFrame_errorTitleTextSize, 15);

        errorStateContentTextSize =
                typedArray.getDimensionPixelSize(R.styleable
                        .LoadIndicatorFrame_errorContentTextSize, 14);

        errorStateTitleTextColor =
                typedArray.getColor(R.styleable.LoadIndicatorFrame_errorTitleTextColor,
                        Color.BLACK);

        errorStateContentTextColor =
                typedArray.getColor(R.styleable
                        .LoadIndicatorFrame_errorContentTextColor, Color.BLACK);

        errorStateButtonTextColor =
                typedArray.getColor(R.styleable
                        .LoadIndicatorFrame_errorButtonTextColor, Color.BLACK);

        errorStateBackgroundColor =
                typedArray.getColor(R.styleable
                        .LoadIndicatorFrame_errorBackgroundColor, Color.TRANSPARENT);

        typedArray.recycle();

        currentBackground = this.getBackground();
    }

    @Override
    protected void onFinishInflate() {
        //检查有没有自定义的indicate view
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getTag() == null) {
                continue;
            }
            if (child.getTag().equals(TAG_LOADING)) {
                /*
                if (loadingStateRelativeLayout != null) {
                    throw new IllegalStateException(this.getClass().getSimpleName() + " should not define multi
                    ide_loading view");
                }
                */
                loadingStateRelativeLayout = child;
                useDefaultLoadingStateLayout = false;
            } else if (child.getTag().equals(TAG_EMPTY)) {
                /*
                if (emptyStateRelativeLayout != null) {
                    throw new IllegalStateException(this.getClass().getSimpleName() + " should not define multi empty
                     view");
                }
                */
                emptyStateRelativeLayout = child;
                useDefaultEmptyStateLayout = false;
            }
            if (child.getTag().equals(TAG_ERROR)) {
                /*
                if (errorStateRelativeLayout != null) {
                    throw new IllegalStateException(this.getClass().getSimpleName() + " should not define multi error
                     view");
                }
                */
                errorStateRelativeLayout = child;
                useDefaultErrorStateLayout = false;
            }
        }
        super.onFinishInflate();
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) &&
                !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR))) {

            contentViews.add(child);
        }
    }

    /**
     * Hide all other states and show content
     */
    public void showContent() {
        switchState(CONTENT, 0, null, null, null, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Hide all other states and show content
     *
     * @param skipIds Ids of views not to show
     */
    public void showContent(List<Integer> skipIds) {
        switchState(CONTENT, 0, null, null, null, null, null, skipIds);
    }

    /**
     * Hide content and show the progress bar
     */
    public void showLoading() {
        switchState(LOADING, 0, null, null, null, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Hide content and show the progress bar
     *
     * @param skipIds Ids of views to not hide
     */
    public void showLoading(List<Integer> skipIds) {
        switchState(LOADING, 0, null, null, null, null, null, skipIds);
    }

    /**
     * 使用默认的UI显示内容空提示
     */
    public void showEmpty() {
        showEmpty(ResourcesCompat.getDrawable(getResources(), R.mipmap
                        .load_indicator_frame_empty, null),
                getResources().getString(R.string.loadIndicatorFrameEmptyTitle),
                null);
    }

    /**
     * 指定没有数据时提示的文字
     *
     * @param emptyTextTitle   标题 为空不显示
     * @param emptyTextContent 详细 为空不显示
     */
    public void showEmpty(String emptyTextTitle, String emptyTextContent) {
        showEmpty(ResourcesCompat.getDrawable(getResources(), R.mipmap
                        .load_indicator_frame_empty, null),
                emptyTextTitle,
                emptyTextContent);
    }

    /**
     * Show empty view when there are not data to show
     *
     * @param emptyImageDrawable Drawable to show
     * @param emptyTextTitle     Title of the empty view to show
     * @param emptyTextContent   Content of the empty view to show
     */
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextTitle, String emptyTextContent) {
        switchState(EMPTY, 0, emptyImageDrawable, emptyTextTitle, emptyTextContent, null, null, Collections
                .<Integer>emptyList());
    }

    /**
     * Show empty view when there are not data to show
     *
     * @param emptyImageDrawable Drawable to show
     * @param emptyTextTitle     Title of the empty view to show
     * @param emptyTextContent   Content of the empty view to show
     * @param skipIds            Ids of views to not hide
     */
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextTitle, String emptyTextContent, List<Integer>
            skipIds) {
        switchState(EMPTY, 0, emptyImageDrawable, emptyTextTitle, emptyTextContent, null, null, skipIds);
    }

    /**
     * 当加载过程中出错的时候，使用默认的图标和文字显示提示信息
     *
     * @param onClickListener Listener of the error view button
     */
    public void showError(OnClickListener onClickListener) {
        showError(ResourcesCompat.getDrawable(getResources(), R.mipmap.pic_place_holder_error,
                null),
                getResources().getString(R.string.loadIndicatorFrameErrorTitle),
                null,
                getResources().getString(R.string.loadIndicatorFrameErrorButton),
                onClickListener);
    }

    /**
     * Show error view with a button when something goes wrong and prompting the user to try again
     *
     * @param errorImageDrawable Drawable to show
     * @param errorTextTitle     Title of the error view to show
     * @param errorTextContent   Content of the error view to show
     * @param errorButtonText    Text on the error view button to show
     * @param onClickListener    Listener of the error view button
     */
    public void showError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent, String
            errorButtonText, OnClickListener onClickListener) {
        switchState(ERROR, 0, errorImageDrawable, errorTextTitle, errorTextContent, errorButtonText, onClickListener,
                Collections.<Integer>emptyList());
    }

    /**
     * Show error view with a button when something goes wrong and prompting the user to try again
     *
     * @param errorImageDrawable Drawable to show
     * @param errorTextTitle     Title of the error view to show
     * @param errorTextContent   Content of the error view to show
     * @param errorButtonText    Text on the error view button to show
     * @param onClickListener    Listener of the error view button
     * @param skipIds            Ids of views to not hide
     */
    public void showError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent, String
            errorButtonText, OnClickListener onClickListener, List<Integer> skipIds) {
        switchState(ERROR, 0, errorImageDrawable, errorTextTitle, errorTextContent, errorButtonText, onClickListener,
                skipIds);
    }

    /**
     * 当遇到错误时，显示自定义的错误提示layout
     */
    public void showError() {
        showError(null, null, null, null, null);
    }

    /**
     * 显示错误提示占位页面
     *
     * @param errorLayout 错误布局
     * @param hintText    提示信息
     */
    public void showError(@LayoutRes int errorLayout, String hintText, OnClickListener onClickListener) {
        if (TextUtils.isEmpty(hintText)) hintText = getResources().getString(R.string.loadIndicatorFrameErrorTitle);
        switchState(ERROR, errorLayout, ResourcesCompat.getDrawable(getResources(), R.mipmap.pic_place_holder_error,
                null),
                hintText,
                getContext().getString(R.string.click_screen_and_try_again),
                null,
                onClickListener, null);
    }

    /**
     * Get which state is set
     *
     * @return State
     */
    public String getState() {
        return state;
    }

    /**
     * Check if content is shown
     *
     * @return boolean
     */
    public boolean isContent() {
        return state.equals(CONTENT);
    }

    /**
     * Check if ide_loading state is shown
     *
     * @return boolean
     */
    public boolean isLoading() {
        return state.equals(LOADING);
    }

    /**
     * Check if empty state is shown
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return state.equals(EMPTY);
    }

    /**
     * Check if error state is shown
     *
     * @return boolean
     */
    public boolean isError() {
        return state.equals(ERROR);
    }

    private void switchState(String state, @LayoutRes int layoutId, Drawable drawable, String errorText, String
            errorTextContent, String errorButtonText, OnClickListener onClickListener, List<Integer> skipIds) {
        this.state = state;

        switch (state) {
            case CONTENT:
                //Hide all state views to display content
                hideLoadingView();
                hideEmptyView();
                hideErrorView();

                setContentVisibility(true, skipIds);
                break;
            case LOADING:
                hideEmptyView();
                hideErrorView();

//                setLoadingView();
//                setContentVisibility(false, skipIds);
                break;
            case EMPTY:
                hideLoadingView();
                hideErrorView();

                setEmptyView();
                if (useDefaultEmptyStateLayout) {
                    emptyStateImageView.setImageDrawable(drawable);
                    emptyStateTitleTextView.setText(errorText);
                    if (errorTextContent != null) {
                        emptyStateContentTextView.setText(errorTextContent);
                        emptyStateContentTextView.setVisibility(VISIBLE);
                    } else {
                        emptyStateContentTextView.setVisibility(GONE);
                    }
                }
                setContentVisibility(false, skipIds);
                break;
            case ERROR:
                hideLoadingView();
                hideEmptyView();

                setErrorView(layoutId);
                if (useDefaultErrorStateLayout) {
                    errorStateImageView.setImageDrawable(drawable);
                    errorStateTitleTextView.setText(errorText);
                    if (!TextUtils.isEmpty(errorTextContent)) {
                        errorStateContentTextView.setText(errorTextContent);
                        errorStateContentTextView.setVisibility(VISIBLE);
                    } else {
                        errorStateContentTextView.setVisibility(GONE);
                    }
                    if (!TextUtils.isEmpty(errorButtonText)) {
                        errorStateButton.setText(errorButtonText);
                        errorStateButton.setVisibility(VISIBLE);
                        errorStateButton.setOnClickListener(onClickListener);
                    } else {
                        errorStateButton.setVisibility(GONE);
                    }
                    if (errorStateRelativeLayout != null) {
                        errorStateRelativeLayout.setOnClickListener(onClickListener);
                    }
                }
                setContentVisibility(false, skipIds);
                break;
        }
    }

    private void setLoadingView() {
        if (loadingStateRelativeLayout == null) {
            /* LiGang  this is the origin progress load view
            view = inflater.inflate(R.layout.progress_loading_view, null);
            loadingStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.loadingStateRelativeLayout);
            loadingStateRelativeLayout = (RelativeLayout) view;
            loadingStateRelativeLayout.setTag(TAG_LOADING);

            loadingStateProgressBar = (ProgressBar) view.findViewById(R.id.loadingStateProgressBar);

            loadingStateProgressBar.getLayoutParams().width = loadingStateProgressBarWidth;
            loadingStateProgressBar.getLayoutParams().height = loadingStateProgressBarHeight;
            loadingStateProgressBar.requestLayout();
            */
            loadingStateRelativeLayout = inflater.inflate(R.layout.load_view, null,
                    false);
            ProgressBar progressBar = (ProgressBar) loadingStateRelativeLayout.findViewById(R.id.pb);
            //6.0以上ProgressBar替代clip加载动画
            if (Build.VERSION.SDK_INT > 22) {
                final Drawable drawable = getContext().getApplicationContext().getResources()
                        .getDrawable(R.drawable.loading);
                progressBar.setIndeterminateDrawable(drawable);
            }
            loadingStateRelativeLayout.setTag(TAG_LOADING);

            //Set background color if not TRANSPARENT
            if (loadingStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(loadingStateBackgroundColor);
            }

            addView(loadingStateRelativeLayout);
        } else {
            loadingStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setEmptyView() {
        if (emptyStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.progress_empty_view, null);
            //emptyStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.emptyStateRelativeLayout);
            emptyStateRelativeLayout = view;
            emptyStateRelativeLayout.setTag(TAG_EMPTY);

            emptyStateImageView = (ImageView) view.findViewById(R.id.emptyStateImageView);
            emptyStateTitleTextView = (TextView) view.findViewById(R.id
                    .emptyStateTitleTextView);
            emptyStateContentTextView = (TextView) view.findViewById(R.id
                    .emptyStateContentTextView);

            //Set empty state image width and height
            emptyStateImageView.getLayoutParams().width = emptyStateImageWidth;
            emptyStateImageView.getLayoutParams().height = emptyStateImageHeight;
            emptyStateImageView.requestLayout();

            emptyStateTitleTextView.setTextSize(emptyStateTitleTextSize);
            emptyStateContentTextView.setTextSize(emptyStateContentTextSize);
            emptyStateTitleTextView.setTextColor(emptyStateTitleTextColor);
            emptyStateContentTextView.setTextColor(emptyStateContentTextColor);

            //Set background color if not TRANSPARENT
            if (emptyStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(emptyStateBackgroundColor);
            }

            addView(emptyStateRelativeLayout);
        } else {
            emptyStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    @SuppressLint("ResourceType")
    private void setErrorView(@LayoutRes int layoutId) {
        if (errorStateRelativeLayout == null) {
            layoutId = layoutId <= 0 ? R.layout.progress_error_view : layoutId;
            view = inflater.inflate(layoutId, null);
            //errorStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.errorStateRelativeLayout);
            errorStateRelativeLayout = view;
            errorStateRelativeLayout.setTag(TAG_ERROR);

            errorStateImageView = (ImageView) view.findViewById(R.id.errorStateImageView);
            errorStateTitleTextView = (TextView) view.findViewById(R.id
                    .errorStateTitleTextView);
            errorStateContentTextView = (TextView) view.findViewById(R.id
                    .errorStateContentTextView);
            errorStateButton = (Button) view.findViewById(R.id.errorStateButton);
            errorStateClickImageView = ((ImageView) view.findViewById(R.id.iv_net_error));

            //Set error state image width and height
            errorStateImageView.getLayoutParams().width = errorStateImageWidth;
            errorStateImageView.getLayoutParams().height = errorStateImageHeight;
            errorStateImageView.requestLayout();

            errorStateTitleTextView.setTextSize(errorStateTitleTextSize);
            errorStateContentTextView.setTextSize(errorStateContentTextSize);
            errorStateTitleTextView.setTextColor(errorStateTitleTextColor);
            errorStateContentTextView.setTextColor(errorStateContentTextColor);
            errorStateButton.setTextColor(errorStateButtonTextColor);
            errorStateButton.setBackgroundResource(R.drawable.status_btn_shape);

            //Set background color if not TRANSPARENT
            if (errorStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(errorStateBackgroundColor);
            }
            addView(errorStateRelativeLayout);
        } else {
            errorStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setContentVisibility(boolean visible, List<Integer> skipIds) {
        if(skipIds==null) {return;}
        for (View v : contentViews) {
            if (!skipIds.contains(v.getId())) {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }
    }

    public void hideLoadingView() {
        if (loadingStateRelativeLayout != null) {
            loadingStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (loadingStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
            }
        }
    }

    private void hideEmptyView() {
        if (emptyStateRelativeLayout != null) {
            emptyStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (emptyStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
                ;
            }
        }
    }

    public void hideErrorView() {
        if (errorStateRelativeLayout != null) {
            errorStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (errorStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
                ;
            }

        }
    }
}
