package info.sajib.moviesapp.recyclertouchlistner;

import android.view.View;

public interface ClickListner {
    void OnClick(View v, int Position);
    void OnLongClick(View v, int Position);
    void OnScroll();
}
