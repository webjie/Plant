package person.jack.plant.activity;


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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.PlantsDao;
import person.jack.plant.db.entity.Plants;
import person.jack.plant.fragment.BufferKnifeFragment;
import person.jack.plant.fragment.DemoPtrFragment;
import person.jack.plant.fragment.MainPagerFragment;
import person.jack.plant.utils.Utils;

public class PlantUpdateActivity extends BaseFragmentActivity implements View.OnClickListener{

    private  static final String TAG="plant";
    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;
    private EditText et_plantName;
    private Spinner spn_plantLive;
    private TextView tv_plantDate,tv_plantLive,tv_plantType;
    private Button btn_plantUpdate;
    private String growthState = "l";
    private PlantsDao plantsDao;
    private ImageView img_PlantImg;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO=2;
    private String chooseImagePath="null";
    private Uri imageUri;
    private Plants plant;
    private List<Plants> list=new ArrayList<>();

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_plant_update);
        ButterKnife.bind(this);
        plantsDao = new PlantsDao(AppContext.getInstance());
        String plantName=getIntent().getStringExtra("plantname");
        if(plantName!=null){
            Log.d(TAG, "onCreate: 查找植物");
            plant=plantsDao.findByName(plantName);
        }

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        try{
            int result=getIntent().getIntExtra("result",0);
            Log.d("UpdateActivity",result+"");
            if(result==3){
                String type=getIntent().getStringExtra("type");
                Log.d("UpdateActivity",type+"类型");
                tv_plantType.setText(type);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView() {
        textHeadTitle.setText("植物更新");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_plantName = (EditText) findViewById(R.id.et_plantName);
        et_plantName.setText(plant.getName());
        spn_plantLive = (Spinner) findViewById(R.id.spn_plantLive);
        tv_plantDate = (TextView)findViewById(R.id.tv_plantDate);
        tv_plantLive = (TextView)findViewById(R.id.tv_plantLive);
        tv_plantType= (TextView)findViewById(R.id.tv_plantType);
        tv_plantType.setOnClickListener(this);
        tv_plantType.setText(plant.getType());
        tv_plantLive.setText(plant.getGrowthStage());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        tv_plantDate.setText(format.format(new Date()));
        btn_plantUpdate= (Button) findViewById(R.id.btn_plantUpdate);
        btn_plantUpdate.setOnClickListener(this);
        tv_plantDate.setOnClickListener(this);
        tv_plantLive.setOnClickListener(this);
        spn_plantLive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                growthState = spn_plantLive.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        img_PlantImg = (ImageView) findViewById(R.id.img_PlantImg);
        img_PlantImg.setOnClickListener(this);
        if (plant.getImage() != null) {
            File file = new File(plant.getImage());
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(plant.getImage());
                img_PlantImg.setImageBitmap(bitmap);
               chooseImagePath=plant.getImage();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_plantLive:
                tv_plantLive.setVisibility(View.GONE);
                spn_plantLive.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_plantType:
                Intent intent=new Intent(PlantUpdateActivity.this,PlantInfoActivity.class);
                intent.putExtra("result",2);
                startActivity(intent);
                break;
            case R.id.tv_plantDate:
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(PlantUpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear , dayOfMonth);
                        tv_plantDate.setText(DateFormat.format("yyyy-MM-dd", c) + "");
                        Log.d(TAG, DateFormat.format("yyyy年MM月dd日", c) + "");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;

            case R.id.img_PlantImg:
                try{
                    showDialog();;
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case R.id.btn_plantUpdate:
                try{
                    update();
                }catch (Exception e){
                    e.printStackTrace();
                }


                break;
        }
    }

    /**
     * 弹出对话框，选择植物图片
     */
    public void showDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(PlantUpdateActivity.this);
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
     * 更新植物信息
     */
    private void update(){
        String plantName = et_plantName.getText().toString().trim();
        String type=tv_plantType.getText().toString().trim();
        if (plantName.equals("")||type.equals("")) {
            Toast.makeText(PlantUpdateActivity.this, "植物名称不能为空", Toast.LENGTH_SHORT).show();
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
                plant.setName(plantName);
                plant.setPlantingDate(date);
                plant.setImage(chooseImagePath);
                plant.setGrowthStage(growthState);
                plant.setType(type);

                plantsDao.updatePlant(plant);
                Toast.makeText(PlantUpdateActivity.this, "更新植物信息成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PlantUpdateActivity.this,MainActivity.class);
                startActivity(intent);
            }else{
                if(et_plantName.getText().toString().equals(plant.getName())){
                    plant.setName(plantName);
                    plant.setPlantingDate(date);
                    plant.setImage(chooseImagePath);
                    plant.setGrowthStage(growthState);
                    plant.setType(type);
                    plantsDao.updatePlant(plant);
                    Toast.makeText(PlantUpdateActivity.this, "更新植物信息成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PlantUpdateActivity.this,MainActivity.class);
                intent.putExtra("result",1);
                    startActivity(intent);
                }else{
                    Toast.makeText(PlantUpdateActivity.this, "更新植物信息失败，没有此植物名称信息", Toast.LENGTH_SHORT).show();
                }

            }

            list=plantsDao.findAll();
            if(list!=null){
                for (Plants p:list) {
                    Log.d(TAG,"图片路径"+p.getImage()+" ,   名称"+p.getName());
                }
            }

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
                    Toast.makeText(getApplicationContext(), "你没有权限打开相机", Toast.LENGTH_SHORT).show();
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
        if (DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
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
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, selection, null, null);
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
            Toast.makeText(getApplicationContext(), "寻找图片失败", Toast.LENGTH_SHORT).show();
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
            imageUri= FileProvider.getUriForFile(getApplicationContext(),"person.jack.plant.fileprovider",outputImage);
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
