package ts.trainticket.fragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.dd.CircularProgressButton;
import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.AboutActivity;
import ts.trainticket.ContactsActivity;
import ts.trainticket.LoginActivity;
import ts.trainticket.MoreActivity;
import ts.trainticket.OrdersActivity;
import ts.trainticket.R;
import ts.trainticket.SalesTimeActivity;
import ts.trainticket.SettingsActivity;
import ts.trainticket.SignUpActivity;
import ts.trainticket.configUtils.Auth;
import ts.trainticket.httpUtils.ResponseResult;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.ApplicationPreferences;
import ts.trainticket.utils.HttpThread;
import ts.trainticket.utils.RxDownloadUtils;
import ts.trainticket.utils.Utils;

public class AccountFragement extends BaseFragment implements View.OnClickListener {

    // 七牛云
    private static String AccessKey = "T4_MyOOeC258nL7mNcsSav2MXoC9CVw-LVY-ozEe";//此处填你自己的AccessKey
    private static String SecretKey = "PZjVwQEClbg9pC3QyFbkHeZN-TaWFwsqlM8HRjyz";//此处填你自己的SecretKey
    private static String domainKey = "http://p6jsga0vv.bkt.clouddn.com";

    private TextView title_personinfo_tv;
    private ImageView messageReceiveBtn = null;

    private Handler handler;

    private ImageView pic_persionImg = null;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    // test
    private CircularProgressButton button = null;
    private TextView tv = null;
    private TextView userNameTV = null;
    private TextView userPhoneTV = null;

    // 控制面板是否显示
    private LinearLayout loginPanel_iid = null;
    private LinearLayout userInfo_panel_iid = null;
    // 编辑信息
    private ImageView edit_accountBtn = null;
    // 下面的条目按钮
    private LinearLayout contacts_mid = null; //
    private LinearLayout orders_mid = null;  //
    private LinearLayout time_mid = null;
    private LinearLayout paly_mid = null;
    private LinearLayout about_mid = null;
    private LinearLayout logout_mid = null;//


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_account, container, false);
        initViews(view);
        // initSendData();
        return view;
    }

    private void initViews(View view) {
        title_personinfo_tv = (TextView) view.findViewById(R.id.title_personinfo_tv);
        title_personinfo_tv.setText("Account");

        pic_persionImg = (ImageView) view.findViewById(R.id.pic_persion);
        pic_persionImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });


        messageReceiveBtn = (ImageView) view.findViewById(R.id.message_receive_id);
        messageReceiveBtn.setOnClickListener(this);
        // button = (CircularProgressButton) view.findViewById(R.id.rxandroid_btn);
        //  tv = (TextView) view.findViewById(R.id.rxandroid_showresult);
        loginPanel_iid = (LinearLayout) view.findViewById(R.id.loginPanel_iid);
        userInfo_panel_iid = (LinearLayout) view.findViewById(R.id.userInfo_panel_iid);
        edit_accountBtn = (ImageView) view.findViewById(R.id.edit_account_msgId);
        edit_accountBtn.setOnClickListener(this);

        userNameTV = (TextView) view.findViewById(R.id.userName_aid);
        userPhoneTV = (TextView) view.findViewById(R.id.userPhone_aid);

        contacts_mid = (LinearLayout) view.findViewById(R.id.contacts_mid);
        orders_mid = (LinearLayout) view.findViewById(R.id.orders_mid);
        time_mid = (LinearLayout) view.findViewById(R.id.times_mid);
        paly_mid = (LinearLayout) view.findViewById(R.id.play_mid);
        about_mid = (LinearLayout) view.findViewById(R.id.about_mid);
        logout_mid = (LinearLayout) view.findViewById(R.id.logout_mid);

        loginPanel_iid.setOnClickListener(this);

        contacts_mid.setOnClickListener(this);
        orders_mid.setOnClickListener(this);
        time_mid.setOnClickListener(this);
        paly_mid.setOnClickListener(this);
        about_mid.setOnClickListener(this);
        logout_mid.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ApplicationPreferences.isUserOnLine(getContext())) {
            showUserInfoPanel();
            showUserInfo();
            showMenuOrNot(true);
        } else {
            showLoginPanel();
            showMenuOrNot(false);
        }
        boolean isLogin = ApplicationPreferences.isUserOnLine(getContext());
        if (isLogin == true) {
            // /每次都从数据库里拼接url,以求得到的是最新的
                String headPicSrc = ApplicationPreferences.getOneInfo(getContext(), "headPicSrc");
                System.out.println("headPicSrc" + headPicSrc);
                if (headPicSrc != null && headPicSrc != "") {
                    handler = new Handler();
                    new HttpThread(domainKey+"/"+ headPicSrc, pic_persionImg, handler).start();
                }
            }
    }

    private void showLoginPanel() {
        userInfo_panel_iid.setVisibility(View.GONE);
        loginPanel_iid.setVisibility(View.VISIBLE);
        pic_persionImg.setImageResource(R.drawable.man_pic);
    }

    private void showUserInfoPanel() {
        userInfo_panel_iid.setVisibility(View.VISIBLE);
        loginPanel_iid.setVisibility(View.GONE);
    }

    private void showMenuOrNot(boolean showOrNot) {
        if (showOrNot) {
            contacts_mid.setVisibility(View.VISIBLE);
            orders_mid.setVisibility(View.VISIBLE);
            logout_mid.setVisibility(View.VISIBLE);
        } else {
            contacts_mid.setVisibility(View.GONE);
            orders_mid.setVisibility(View.GONE);
            logout_mid.setVisibility(View.GONE);
        }
    }

    // 登录后返回的信息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!ApplicationPreferences.isUserOnLine(getContext())) {
            showLoginPanel();
            return;
        }
        showUserInfoPanel();
        showUserInfo();
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(resultCode + "--------" + requestCode);
        if (requestCode != -1) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    try {
                        startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        try {
                            setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    private void showUserInfo() {
        String userName = ApplicationPreferences.getOneInfo(getContext(), ApplicationPreferences.USER_NAME);
        String userPhone = ApplicationPreferences.getOneInfo(getContext(), ApplicationPreferences.USER_PHONE);
        userNameTV.setText(userName);
        userPhoneTV.setText(userPhone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contacts_mid:
                startActivity(new Intent(getActivity(), ContactsActivity.class));
                break;
            case R.id.orders_mid:
                startActivity(new Intent(getActivity(), OrdersActivity.class));
                break;
            case R.id.times_mid:
                startActivity(new Intent(getActivity(), SalesTimeActivity.class));
                break;
            case R.id.loginPanel_iid:
                // startActivity(new Intent(getActivity(),LoginActivity.class));
                startActivityForResult(new Intent(getContext(), LoginActivity.class), LoginActivity.SIGN_IN_REQUEST_CODE);
                break;
            case R.id.logout_mid:
                //退出登录
                AlertDialogPro.Builder builder = new AlertDialogPro.Builder(getContext());
                builder.setMessage("Are you sure you want to log out?").setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApplicationPreferences.clearLoginInfo(getContext());
                        showLoginPanel();
                        showMenuOrNot(false);
                    }
                }).setNegativeButton("Cancel", null).show();
                break;
            case R.id.about_mid:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.play_mid:
                startActivity(new Intent(getActivity(), MoreActivity.class));
                break;
            case R.id.edit_account_msgId:
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                break;
            case R.id.message_receive_id:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            default:
                break;

        }
    }


    private void initSendData() {
        button.setIndeterminateProgressMode(true);
        button.setOnClickListener(new GetDataListener());
        addToBtnController(button);
    }

    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Setting head sculpture");
        String[] items = {"Select local photos", "Photograph"};
        builder.setNegativeButton("Cancel", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) throws IOException {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            pic_persionImg.setImageBitmap(photo);
            uploadPic(photo);
            saveFile(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String imagePath = Utils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath + "");
        if (imagePath != null) {
            // 拿着imagePath上传了
            // ...
            uploadImg2QiNiu(imagePath);

        }

    }


    /**
     * 保存文件
     *
     * @param bm
     * @throws IOException
     */
    public File saveFile(Bitmap bm) throws IOException {
        String path = Environment.getExternalStorageDirectory().toString() + "/train/icon_bitmap/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        path = path + "myicon.jpg";
        System.out.println(path);
        ApplicationPreferences.setOneInfo(getContext(), "localPic", path);
        File myIconFile = new File(path);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myIconFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myIconFile;
    }

    /**
     * 从本地获取图片
     *
     * @param pathString 文件路径
     * @return 图片
     */
    public Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
                bitmap = Utils.toRoundBitmap(bitmap, tempUri); // 这个时候的图片已经被处理成圆形的了
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    private void uploadImg2QiNiu(String picPath) {
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "icon_" + sdf.format(new Date());
      //  String key = "icon_headpic";
        System.out.println(key + "-0000000000099999");
        uploadManager.put(picPath, key, Auth.create(AccessKey, SecretKey).uploadToken("imook-img"), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                // info.error中包含了错误信息，可打印调试
                // 上传成功后将key值上传到自己的服务器
                System.out.println(info.isOK() + "-01211999" + info.error);
                if (info.isOK()) {
                    System.out.println("token===" + Auth.create(AccessKey, SecretKey).uploadToken("photo"));
                    String headpicPath = "http://p6jsga0vv.bkt.clouddn.com/" + key;
                    System.out.println("complete: " + headpicPath);
                    ApplicationPreferences.setOneInfo(getContext(), "headPicSrc", key);

//                    String headPicSrc = ApplicationPreferences.getOneInfo(getContext(), "headPicSrc");

                    //  如果不剪切，图片不会立即生效，需要重新请求显示
                    if (headpicPath != null && headpicPath != "") {
                        handler = new Handler();
                        new HttpThread(headpicPath, pic_persionImg, handler).start();
                    }
                    System.out.println("-=-==-"+userNameTV.getText().toString());
                    uploadHeadPic(userNameTV.getText().toString(),key);
                   // Toast.makeText(getContext(), "头像上传成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Upload pictures failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    public void uploadHeadPic(String usrName, String picPath) {
        try {
            usrName = URLEncoder.encode(usrName, "UTF-8");
            picPath = URLEncoder.encode(picPath, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
            String getUserUri = UrlProperties.headPicUrl + "/" + usrName + "/" + picPath;
            subscription = RxHttpUtils.getDataByUrl(getUserUri, getContext())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {

                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            unlockClick();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(String responseResult) {
                            unlockClick();
                            if (responseResult != null && !responseResult.equals("")) {
                                Gson gson = new Gson();
                                ResponseResult result = gson.fromJson(responseResult, ResponseResult.class);
                                if(result.isStatus())
                                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity(), "Upload pictures failed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Upload pictures failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
    }


    private class GetDataListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            lockClick();
            button.setProgress(50);
            subscription = RxDownloadUtils.getAllCities("wewe")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseResult>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            button.setProgress(0);
                            unlockClick();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(ResponseResult responseResult) {
                            tv.setText(responseResult.getData().toString());
                            Toast.makeText(getActivity(), responseResult.getMsg(), Toast.LENGTH_SHORT).show();
                            button.setProgress(0);
                            unlockClick();
                        }
                    });
        }
    }
}
