package tgt.com.smartsecretary;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

public class ViewDialogue {

    Activity activity;
    Dialog dialog;
    public ViewDialogue(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {

        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_logo);
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }

}