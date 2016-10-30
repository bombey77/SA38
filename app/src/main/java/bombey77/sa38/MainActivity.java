package bombey77.sa38;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvText;
    Button btnStart;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "MyDB";
    private static final String TABLE_NAME = "MyTable";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDB();

        tvText = (TextView) findViewById(R.id.tvTime);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
    }



    void initDB () {
        database = this.openOrCreateDatabase(DATABASE_NAME , MODE_PRIVATE , null);
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(FirstNumber INT , SecondNumber INT , Result INT);" );
        database.delete(TABLE_NAME, null, null);
    }

    @Override
    public void onClick(View v) {
        database.delete(TABLE_NAME, null, null);
        long start = System.currentTimeMillis();
        putInfo();
        long finish = System.currentTimeMillis() - start;
        tvText.setText("Result = " + Long.toString(finish) +" ms");
    }

    void putInfo() {
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?);";
        SQLiteStatement statement = database.compileStatement(sql);
        database.beginTransaction();
        try {
            for (int i = 0; i < 1000; i++) {
                statement.clearBindings();
                statement.bindLong(1, i);
                statement.bindLong(2, i);
                statement.bindLong(3, i*i);
                statement.execute();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
