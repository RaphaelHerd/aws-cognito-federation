package com.gigya.android.sdk.sample.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.gigya.android.sdk.sample.R;

/**
 * Custom view for the input area, including the init button
 */
public class InitScreenInputAreaView extends LinearLayout {

    private static final String TAG = "InputAreaView";

    private static final String DEFAULT_API = "3_JVYu7ywHemCkN5ApgjNccwBx_QfV7Bn7kn2WrL5o82s8f54jjVrGI8Apv3W0DC_Q";
    private TextInputLayout mApikeyWrapper;
    private Spinner mDataCenterSpinner;
    private IInputAreaEvent mInputAreaEvent;
    private Button mBtnInit;

    public interface IInputAreaEvent {
        void onInitBtnClicked(String apiKey, String dataCenter);
    }

    public InitScreenInputAreaView(Context context) {
        this(context, null);
    }

    public InitScreenInputAreaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InitScreenInputAreaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InitScreenInputAreaView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setEventListener(IInputAreaEvent inputAreaEvent) {
        mInputAreaEvent = inputAreaEvent;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();

        //TODO: only for testing
        mApikeyWrapper.getEditText().setText(DEFAULT_API);
    }

    private void initView() {
        mApikeyWrapper = findViewById(R.id.apikey_input_wrapper);
        mDataCenterSpinner = findViewById(R.id.datacenter_spinner);
        mBtnInit = findViewById(R.id.button_init);

        mBtnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apiKey = mApikeyWrapper.getEditText().getText().toString();
                boolean isValidKey = !TextUtils.isEmpty(apiKey);
                changeInputLayout(isValidKey);
                if (isValidKey) {
                    mInputAreaEvent.onInitBtnClicked(apiKey, mDataCenterSpinner.getSelectedItem().toString());
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.datacenter_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDataCenterSpinner.setAdapter(adapter);
    }

    /**
     * @param isValidInput
     */
    private void changeInputLayout(boolean isValidInput) {
        if (isValidInput) {
            mApikeyWrapper.setErrorEnabled(false);
        } else {
            String inputFieldErrorMsg = getContext().getString(R.string.input_field_api_key_error_msg);
            mApikeyWrapper.setError(inputFieldErrorMsg);
        }
    }
}
