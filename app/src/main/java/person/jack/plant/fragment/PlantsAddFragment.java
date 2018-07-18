package person.jack.plant.fragment;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.table.TableUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.transform.URIResolver;

import person.jack.plant.R;
import person.jack.plant.activity.MainActivity;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.PlantsDao;
import person.jack.plant.db.entity.Plants;
import person.jack.plant.http.HttpClient;
import person.jack.plant.model.MyAppContants;
import person.jack.plant.utils.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yanxu on 2018/6/4.
 */

public class PlantsAddFragment extends Fragment implements View.OnClickListener {
    private  static final String TAG="plant";

    private EditText et_plantName;
    private Spinner spn_plantLive;
    private TextView tv_plantDate;
    private Button btn_plantSave,btn_plantUpdate;
    private String growthState = "l";
    private PlantsDao plantsDao;
   private ImageView img_PlantImg;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO=2;
    private String chooseImagePath="null";
    private Uri imageUri;
    private List<Plants> list=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_plantadd, container, false);
        initView(view);
        plantsDao = new PlantsDao(AppContext.getInstance());
        list=plantsDao.findAll();
        if(list!=null){
            for (Plants p:list
                    ) {
                Log.d(TAG,"图片路径"+p.getImage()+"  , 名称"+p.getName());
            }

        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView(View view) {

        et_plantName = (EditText) view.findViewById(R.id.et_plantName);
        spn_plantLive = (Spinner) view.findViewById(R.id.spn_plantLive);


        tv_plantDate = (TextView) view.findViewById(R.id.tv_plantDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        tv_plantDate.setText(format.format(new Date()));
        btn_plantSave = (Button) view.findViewById(R.id.btn_plantSave);
        btn_plantUpdate= (Button) view.findViewById(R.id.btn_plantUpdate);
        btn_plantUpdate.setOnClickListener(this);
        tv_plantDate.setOnClickListener(this);
        btn_plantSave.setOnClickListener(this);
        spn_plantLive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                growthState = spn_plantLive.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        img_PlantImg = (ImageView) view.findViewById(R.id.img_PlantImg);
        img_PlantImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_plantDate:
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear , dayOfMonth);
                        tv_plantDate.setText(DateFormat.format("yyyy-MM-dd", c) + "");
                        Log.d(TAG, DateFormat.format("yyyy年MM月dd日", c) + "");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.btn_plantSave:
               /* if(chooseImagePath==null){
                   chooseImagePath=getResourcesUri(R.drawable.default_image);
                }*/
                    submit();
                break;
            case R.id.img_PlantImg:
                showDialog();;
                break;
            case R.id.btn_plantUpdate:

                update();
                break;
        }
    }

    /**
     * 弹出对话框，选择植物图片
     */
    public void showDialog() {
       AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
       builder.setMessage("请设置植物图片~~");
       builder.setPositiveButton("相册", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               openAlbum();

           }
       });
       builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
             takePhoto();
           }
       });
       builder.show();
    }
    /**
     * 增加植物信息
     */
    private void submit() {
        String plantName = et_plantName.getText().toString().trim();
        if (plantName.equals("")) {
            Toast.makeText(getContext(), "植物名称不能为空", Toast.LENGTH_SHORT).show();
        }  else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(tv_plantDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Plants plants=plantsDao.findByName(plantName);
            Plants plants1=null;
            if(plants==null){
                plants1=new Plants(1,chooseImagePath,plantName,growthState,date);
                plantsDao.add(plants1);
                Toast.makeText(getContext(), "添加植物信息成功", Toast.LENGTH_SHORT).show();
                et_plantName.setText("");
                chooseImagePath="null";
                img_PlantImg.setImageResource(R.drawable.plantlogo);
            }else{
                Toast.makeText(getContext(), "添加失败，植物名称已存在", Toast.LENGTH_SHORT).show();
            }
            list=plantsDao.findAll();
            if(list!=null){
                for (Plants p:list) {
                    Log.d(TAG,"数据库所有植物信息：图片路径"+p.getImage()+"   ,名称"+p.getName());
                }
            }
            try{
                MainActivity mainActivity=(MainActivity)getActivity();

                BufferKnifeFragment bufferKnifeFragment=(BufferKnifeFragment)mainActivity.getSupportFragmentManager().findFragmentByTag("ImFragment");
                if(bufferKnifeFragment!=null){
                    bufferKnifeFragment.setLoadAll(false);
                    bufferKnifeFragment.loadData();

                }
                Utils.getPlantTypeByImage(plants1.getImage());

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    /**
     * 更新植物信息
     */
    private void update(){
        String plantName = et_plantName.getText().toString().trim();
        if (plantName.equals("")) {
            Toast.makeText(getContext(), "植物名称不能为空", Toast.LENGTH_SHORT).show();
        }  else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(tv_plantDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Plants plants=plantsDao.findByName(plantName);
            if(plants==null){
                Toast.makeText(getContext(), "更新植物信息失败，没有此植物名称信息", Toast.LENGTH_SHORT).show();
            }else{
                plants.setName(plantName);
                plants.setPlantingDate(date);
                plants.setImage(chooseImagePath);
                plants.setGrowthStage(growthState);
                plantsDao.updatePlant(plants);
                Toast.makeText(getContext(), "更新植物信息成功", Toast.LENGTH_SHORT).show();
            }

            list=plantsDao.findAll();
            if(list!=null){
                for (Plants p:list) {
                    Log.d(TAG,"图片路径"+p.getImage()+" ,   名称"+p.getName());
                }
            }
            try{
                MainActivity mainActivity=(MainActivity)getActivity();
                MainPagerFragment mainPagerFragment=(MainPagerFragment)mainActivity.getSupportFragmentManager().findFragmentByTag("HomeFragment");
                DemoPtrFragment demoPtrFragment=(DemoPtrFragment)mainPagerFragment.getChildFragmentManager().getFragments().get(1);
                demoPtrFragment.setLoadAll(false);
                demoPtrFragment.loadData();

                BufferKnifeFragment bufferKnifeFragment=(BufferKnifeFragment)mainActivity.getSupportFragmentManager().findFragmentByTag("ImFragment");
                if(bufferKnifeFragment!=null){
                    bufferKnifeFragment.setLoadAll(false);
                    bufferKnifeFragment.loadData();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            et_plantName.setText("");
            chooseImagePath="null";
            img_PlantImg.setImageResource(R.drawable.plantlogo);
        }
    }
    /**
     * 打开相册
     */
    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getContext(), "你没有权限打开相机", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //读取相册
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4以上使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            //拍照
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                   /* try {
                       Bitmap   bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri));
                        img_PlantImg.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }*/
                   cropPhoto(imageUri);
                }
                break;
            case 0X003:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap head = extras.getParcelable("data");
                    img_PlantImg.setImageBitmap(head);
                }
                break;
            default:
                break;
        }
    }
    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 0X003);
    }

    /**
     * 生成图片
     * 系统版本4.4以上
     * @param data
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Log.d("img-----", "执行4.4以上方法");
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //如果document的类型是uri则通过document id 处理
                String id = docId.split(":")[1];//解析出数字格式id
                String selection = MediaStore.Images.Media._ID + "=" + id;
               imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);


            } else if ("com.android.provider.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId
                        (Uri.parse("content:///downloads/public_downloads"), Long.valueOf(docId));

                imagePath = getImagePath(contentUri, null);

            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri则使用普通方式处理
            imagePath = getImagePath(uri, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file 类型的uri 则直接获取图片路径
            imagePath = uri.getPath();


        }
       displayImages(imagePath);
    }
    /**
     * 生成图片
     * 系统版本4.4以下
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Log.d("img-----", "执行4.4以下方法");
        Uri uri = data.getData();
        cropPhoto(uri);
       //String imagePath = getImagePath(uri, null);
        //displayImages(imagePath);
    }
    /**
     *
     * 根据uri、select 获得路径
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        //根据uri和selection 获得真是路径
        String path = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    /**
     * 根据路径将图片转为Bitmap
     * 添加到ImageView
     * @param imagePath
     */
    private void displayImages(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            chooseImagePath = imagePath;
            img_PlantImg.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getContext(), "寻找图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * 拍照
     */
    public void takePhoto(){
       SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
        //创建File对象，用于储存拍照后的图片
        File outputImage = new File(Environment.getExternalStorageDirectory()
                + "/"+format.format(new Date())+".jpg");
        try {
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(getContext(),"person.jack.plant.fileprovider",outputImage);
        }else{
            imageUri= Uri.fromFile(outputImage);
        }
        //取得路径
        chooseImagePath=imageUri.getPath();
        if(chooseImagePath==null){
            Log.d("path-------","null");
        }else{
            Log.d("path-------",chooseImagePath);
        }
        //启动相机程序
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri
        );
        startActivityForResult(intent,TAKE_PHOTO);
    }
    private String getResourcesUri(int id){
        Resources resources=getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }


}
