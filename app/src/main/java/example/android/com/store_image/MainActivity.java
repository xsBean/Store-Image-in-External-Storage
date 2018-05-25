package example.android.com.store_image;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Get the widgets reference from XML layout
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        final Button btn = (Button) findViewById(R.id.btn);
        final ImageView iv_source = (ImageView) findViewById(R.id.iv_source);
        final ImageView iv_saved = (ImageView) findViewById(R.id.iv_saved);
        final TextView tv_saved = (TextView) findViewById(R.id.tv_saved);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the image from drawable resource as drawable object
                Drawable drawable = getResources().getDrawable(R.drawable.birds);

                // Get the bitmap from drawable object
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

                File file;

                // Get the external storage directory path
                PackageManager m = getPackageManager();
                String s = getPackageName();
//                File rootDataDir = getFilesDir();
                String path = null;
//                path = rootDataDir.toString();
//                Log.v("Package Dir", rootDataDir.toString());
                try{
                    PackageInfo info = m.getPackageInfo(s, 0);
                    path = info.applicationInfo.dataDir;
//                    path = rootDataDir.toString();
                    File directory = new File(path+File.separator + "MoviePosters");
                    if(! directory.exists() && !directory.isDirectory()){
                        directory.mkdir();
                    }
                    path = path+File.separator + "MoviePosters";
                    Log.v("Package Dir", path);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();

                    Log.v("Package Dir:" , "Error Package name not found", e);
                }



                /*
                    public File (String dirPath, String name)
                        Constructs a new File using the specified directory path and file name,
                        placing a path separator between the two.

                        Parameters
                            dirPath : the path to the directory where the file is stored.
                            name : the file's name.

                        Throws
                            NullPointerException if name == null.
                */

                // Create a file to save the image
                file = new File(path, "UniqueFileName"+".jpg");

                try{
                    OutputStream stream = null;

                    /*
                        FileOutputStream
                            An output stream that writes bytes to a file. If the output file exists,
                            it can be replaced or appended to. If it does not exist, a new
                            file will be created.
                    */

                    /*
                        public FileOutputStream (File file)
                            Constructs a new FileOutputStream that writes to file. The file will be
                            truncated if it exists, and created if it doesn't exist.

                            Throws
                                FileNotFoundException : if file cannot be opened for writing.
                    */

                    stream = new FileOutputStream(file);

                    /*
                        public boolean compress (Bitmap.CompressFormat format, int quality, OutputStream stream)
                            Write a compressed version of the bitmap to the specified outputstream.
                            If this returns true, the bitmap can be reconstructed by passing a
                            corresponding inputstream to BitmapFactory.decodeStream().

                            Note: not all Formats support all bitmap configs directly, so it is
                            possible that the returned bitmap from BitmapFactory could be in a
                            different bitdepth, and/or may have lost per-pixel alpha
                            (e.g. JPEG only supports opaque pixels).

                            Parameters
                                format : The format of the compressed image
                                quality : Hint to the compressor, 0-100. 0 meaning compress for small
                                    size, 100 meaning compress for max quality. Some formats,
                                    like PNG which is lossless, will ignore the quality setting
                                stream : The outputstream to write the compressed data.
                            Returns
                                true : if successfully compressed to the specified stream.
                    */
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

                    /*
                        public void flush ()
                            Flushes this stream. Implementations of this method should ensure
                            that any buffered data is written out. This implementation does nothing.

                            Throws
                                IOException : if an error occurs while flushing this stream.
                    */
                    stream.flush();

                    /*
                        public void close ()
                            Closes this stream. Implementations of this method should free any
                            resources used by the stream. This implementation does nothing.

                            Throws
                                IOException : if an error occurs while closing this stream.
                    */
                    stream.close();

                }catch (IOException e) // Catch the exception
                {
                    e.printStackTrace();
                }

                // Parse the saved image path to uri
                String absolutePath = file.getAbsolutePath();
                Log.d("File", absolutePath);
                Uri savedImageURI = Uri.parse(absolutePath);

                // Display the saved image to ImageView
                iv_saved.setImageURI(savedImageURI);

                // Display saved image uri to TextView
                tv_saved.setText("Image saved in external storage.\n" + savedImageURI);
            }
        });
    }
}