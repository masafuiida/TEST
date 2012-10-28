package team.asaichi.oneColorMJ;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {
	private ArrayList<ImageView> tehaiViewList = new ArrayList<ImageView>();
	private ImageView tsumohaiView;
	private TileRepository tr;
	private TileImageBuilder tb = new TileImageBuilder();
	private TrophyManager tm;
	private int junme;
	private boolean endFlg;
	private int countSuccess = 0;
	private int countFailure = 0;
	private int clickedTilePos = 0;
	private int clickedTileNumber = 0;
	final int MAX_JUNME = 5;
	private Button agariButton;
	private Button resetButton;
	private CheckBox ripaiCheck;
	private int countWin = 0;
	final String KEY_3RENSHO = "3rensho";
	final String KEY_5RENSHO = "5rensho";
	final String KEY_10RENSHO = "10rensho";
	final String KEY_20RENSHO = "20rensho";
	final String KEY_30RENSHO = "30rensho";
	final String KEY_40RENSHO = "40rensho";
	final String KEY_50RENSHO = "50rensho";
	final String KEY_60RENSHO = "60rensho";
	final String KEY_70RENSHO = "70rensho";
	final String KEY_80RENSHO = "80rensho";
	final String KEY_90RENSHO = "90rensho";
	final String KEY_100RENSHO = "100rensho";
	final String KEY_TENHO = "tenho";
	final String KEY_SUANKO = "suanko";
	final String KEY_CHUREN = "churen";
	final String KEY_DAISHARIN = "daisharin";
	final String KEY_RYUISO = "ryuiso";
	final String KEY_HYAKUMANGOKU = "hyakumangoku";
	enum TileType{
		man,pin,sou
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tm = new TrophyManager(getSharedPreferences("pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE));
        
        //手牌View配列の設定
        setTehaiViewList();
        
        //手牌の初期化
        initTiles();
        
        
        
        /*
        final Dialog dialog = new Dialog(this);
		dialog.setTitle("Dialog");
		dialog.setContentView(R.layout.trophy);
		dialog.show();
		*/
		
    }
    
    /**
     * 手牌表示Viewの初期設定
     */
    private void setTehaiViewList() {
    	String[] itemList = getResources().getStringArray(R.array.tehaiViews);
    	for(int i = 0; i <= itemList.length - 1; i++){
    		tehaiViewList.add((ImageView) findViewById(getResources().getIdentifier(itemList[i], "id", getPackageName())));
    		tehaiViewList.get(i).setOnClickListener(this);
    	}
    	tsumohaiView = (ImageView) findViewById(R.id.tsumohaiView);
    	tsumohaiView.setOnClickListener(this);
    	agariButton = (Button) findViewById(R.id.agariButton);
    	resetButton = (Button) findViewById(R.id.resetButton);
    	ripaiCheck = (CheckBox) findViewById(R.id.ripaiCheck);
    }
    
    /**
     * 手牌表示
     */
    private void showTiles() {
    	for(int i = 1; i <= 13; i++) {
    		tehaiViewList.get(i - 1).setImageDrawable(getTileImage(tr.getTile(i)));
    	}
    }
    
    /**
     * ツモ牌表示
     */
    private void showNextTile() {
    	tsumohaiView.setImageDrawable(getTileImage(tr.getNextTile()));
    	junme++;
    	showJunme();
    	clickedTilePos = 0;
    	clickedTileNumber = 0;
    }
    
    /**
     * 牌画取得
     * @param num 数字
     * @return 牌画イメージ
     */
    private Drawable getTileImage(int num) {
    	//TypedArray images = getResources().obtainTypedArray(R.array.tileImages);
    	//return images.getDrawable(num - 1);
    	return tb.getTileImage(num);
    }
    
    private Drawable getTileImageLarge(int num) {
    	return tb.getTileImageLarge(num);
    }
    
	@Override
	public void onClick(View v) {
		if(endFlg) {
			return;
		}
		
		if(!tr.hasNextTile() || junme == 5) {
			Toast.makeText(this, "流局", Toast.LENGTH_SHORT).show();
			countFailure++;
			countWin = 0;
			endFlg = true;
			showResult();
			return;
		}
		
		if(clickedTilePos > 0) {
			if(clickedTilePos == 14) {
				tsumohaiView.setImageDrawable(getTileImage(clickedTileNumber));
			}
			else {
				tehaiViewList.get(clickedTilePos - 1).setImageDrawable(getTileImage(clickedTileNumber));
			}
		}
		
		for(int i = 1; i <= 13; i++) {
			if (tehaiViewList.get(i -1) == v) {
				if(i == clickedTilePos) {
					releaseTile(i);
				}
				else {
					tehaiViewList.get(i - 1).setImageDrawable(getTileImageLarge(tr.getTile(i)));
					
					clickedTilePos = i;
					clickedTileNumber = tr.getTile(i);
				}
				return;
			}
		}
		if(clickedTilePos == 14) {
			tsumogiri();
		}
		else {
			tsumohaiView.setImageDrawable(getTileImageLarge(tr.nextTile));
			clickedTilePos = 14;
			clickedTileNumber = tr.nextTile;
		}
	}
	
	/**
	 * 牌を切る
	 * @param i 切る牌の位置
	 */
	private void releaseTile(int i) {
		tr.releaseTile(i);
		showTiles();
		showNextTile();
	}
	
	/**
	 * ツモ切り
	 */
	private void tsumogiri() {
		showNextTile();
	}
	
	/**
	 * 和了ボタンクリック
	 * @param v
	 */
	public void onAgariClick(View v){
		
		if(endFlg) {
			return;
		}
		
		if(tr.isGoneOut()) {
			Toast.makeText(this, "おめでとう", Toast.LENGTH_SHORT).show();
			countSuccess++;
			countWin++;
			checkTrophy();
		}
		else {
			Toast.makeText(this, "チョンボ", Toast.LENGTH_SHORT).show();
			countFailure++;
			countWin = 0;
		}
		endFlg = true;
		showResult();
		
	}
	
	/**
	 * 最初からボタンクリック
	 * @param v
	 */
	public void onResetClick(View v){
		initTiles();
	}
	
	/**
	 * 巡目表示
	 */
	private void showJunme() {
		TextView text = (TextView)findViewById(R.id.junmeText);
		//text.setText(Integer.toString(junme) + "巡目");
		text.setText("残り " + Integer.toString(MAX_JUNME - junme) + " 巡");
	}
	
	/**
	 * 手牌の初期化
	 */
	private void initTiles() {
		tr = new TileRepository(!this.ripaiCheck.isChecked());
		
		junme = 0;
		
		endFlg = false;
		
		agariButton.setVisibility(View.VISIBLE);
		resetButton.setVisibility(View.INVISIBLE);
		
		//配牌の表示
        showTiles();
        
        //第1自摸
        showNextTile();
	}
	
	/**
	 * 結果の表示
	 */
	private void showResult() {
		TextView text = (TextView)findViewById(R.id.resultText);
		text.setText("成功：" + Integer.toString(countSuccess) + "回　失敗：" + Integer.toString(countFailure) + "回");
		
		agariButton.setVisibility(View.INVISIBLE);
		resetButton.setVisibility(View.VISIBLE);
	}
	
	/**
	 * メニューの追加
	 */
	public boolean onCreateOptionsMenu( Menu menu ) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.mainmenu, menu);
    	return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.tileSelectMenu:
			new AlertDialog.Builder(this)
	          .setTitle("牌種選択")
	          .setItems(R.array.tiles,
	           new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialoginterface,
	                    int i) {
	            	  changeTileType(i);
	              }
	           })
	          .show();
			break;
		case R.id.trophyMenu:
			showTrophyDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void changeTileType(int i) {
		TileType type;
		switch (i) {
		case 0:
			type = TileType.man;
			break;
		case 1:
			type=TileType.pin;
			break;
		case 2:
			type=TileType.sou;
			break;
		default:
			type = TileType.man;
		}
		tb.setType(type);
		showTiles();
		tsumohaiView.setImageDrawable(getTileImage(tr.nextTile));
	}
	
	private void checkTrophy() {
		if(countWin == 3) {
			Toast.makeText(this, "3連勝のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_3RENSHO);
		}
		if(countWin == 5) {
			Toast.makeText(this, "5連勝のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_5RENSHO);
		}
		if(countWin == 10) {
			Toast.makeText(this, "10連勝のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_10RENSHO);
		}
		if(countWin == 20) {
			Toast.makeText(this, "20連勝のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_20RENSHO);
		}
		if(countWin == 30) {
			Toast.makeText(this, "30連勝のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_30RENSHO);
		}
		if(countWin == 40) {
			Toast.makeText(this, "40連勝のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_40RENSHO);
		}
		if(countWin == 50) {
			Toast.makeText(this, "50連勝のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_50RENSHO);
		}
		if(countWin == 100) {
			Toast.makeText(this, "100連勝のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_100RENSHO);
		}
		if(junme == 1) {
			Toast.makeText(this, "天和のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_TENHO);
		}
		if(tr.isSuanko()) {
			Toast.makeText(this, "四暗刻のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_SUANKO);
		}
		if(tr.isChuren()) {
			Toast.makeText(this, "九蓮宝燈のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_CHUREN);
		}
		if(tb.getType() == TileType.pin) {
			if(tr.isDaisharin()) {
				Toast.makeText(this, "大車輪のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
				tm.setTrophy(KEY_DAISHARIN);
			}
		}
		if(tb.getType() == TileType.sou) {
			if(tr.isRyuiso()) {
				Toast.makeText(this, "緑一色のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
				tm.setTrophy(KEY_RYUISO);
			}
		}
		if(tb.getType() == TileType.man) {
			if(tr.isHyakumangoku()) {
				Toast.makeText(this, "百萬石のトロフィーを獲得しました！", Toast.LENGTH_SHORT).show();
				tm.setTrophy(KEY_HYAKUMANGOKU);
			}
		}
	}
	
	private void showTrophyDialog() {
		// レイアウトインフレーター使用
        LayoutInflater factory = LayoutInflater.from(this);
        // 他のレイアウトファイルを指定
        View layInfView = factory.inflate(R.layout.trophy, null);
        
		final Dialog dialog = new Dialog(this);
		
		if(tm.getTrophy(KEY_3RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView1_1)).setText("３連勝");
			((TextView)layInfView.findViewById(R.id.textView1_2)).setText(Integer.toString(tm.getTrophy(KEY_3RENSHO)) + "回");
		}
		if(tm.getTrophy(KEY_5RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView2_1)).setText("５連勝");
			((TextView)layInfView.findViewById(R.id.textView2_2)).setText(Integer.toString(tm.getTrophy(KEY_5RENSHO)) + "回");
		}
		if(tm.getTrophy(KEY_10RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView3_1)).setText("１０連勝");
			((TextView)layInfView.findViewById(R.id.textView3_2)).setText(Integer.toString(tm.getTrophy(KEY_10RENSHO)) + "回");
		}
		if(tm.getTrophy(KEY_20RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView4_1)).setText("２０連勝");
			((TextView)layInfView.findViewById(R.id.textView4_2)).setText(Integer.toString(tm.getTrophy(KEY_20RENSHO)) + "回");
		}
		if(tm.getTrophy(KEY_30RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView5_1)).setText("３０連勝");
			((TextView)layInfView.findViewById(R.id.textView5_2)).setText(Integer.toString(tm.getTrophy(KEY_30RENSHO)) + "回");
		}
		if(tm.getTrophy(KEY_40RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView6_1)).setText("４０連勝");
			((TextView)layInfView.findViewById(R.id.textView6_2)).setText(Integer.toString(tm.getTrophy(KEY_40RENSHO)) + "回");
		}
		if(tm.getTrophy(KEY_50RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView7_1)).setText("５０連勝");
			((TextView)layInfView.findViewById(R.id.textView7_2)).setText(Integer.toString(tm.getTrophy(KEY_50RENSHO)) + "回");
		}
		if(tm.getTrophy(KEY_100RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView8_1)).setText("１００連勝");
			((TextView)layInfView.findViewById(R.id.textView8_2)).setText(Integer.toString(tm.getTrophy(KEY_100RENSHO)) + "回");
		}
		if(tm.getTrophy(KEY_TENHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView9_1)).setText("天和");
			((TextView)layInfView.findViewById(R.id.textView9_2)).setText(Integer.toString(tm.getTrophy(KEY_TENHO)) + "回");
		}
		if(tm.getTrophy(KEY_SUANKO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView10_1)).setText("四暗刻");
			((TextView)layInfView.findViewById(R.id.textView10_2)).setText(Integer.toString(tm.getTrophy(KEY_SUANKO)) + "回");
		}
		if(tm.getTrophy(KEY_CHUREN) > 0) {
			((TextView)layInfView.findViewById(R.id.textView11_1)).setText("九蓮宝燈");
			((TextView)layInfView.findViewById(R.id.textView11_2)).setText(Integer.toString(tm.getTrophy(KEY_CHUREN)) + "回");
		}
		if(tm.getTrophy(KEY_DAISHARIN) > 0) {
			((TextView)layInfView.findViewById(R.id.textView12_1)).setText("大車輪");
			((TextView)layInfView.findViewById(R.id.textView12_2)).setText(Integer.toString(tm.getTrophy(KEY_DAISHARIN)) + "回");
		}
		if(tm.getTrophy(KEY_RYUISO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView13_1)).setText("緑一色");
			((TextView)layInfView.findViewById(R.id.textView13_2)).setText(Integer.toString(tm.getTrophy(KEY_RYUISO)) + "回");
		}
		if(tm.getTrophy(KEY_HYAKUMANGOKU) > 0) {
			((TextView)layInfView.findViewById(R.id.textView14_1)).setText("百萬石");
			((TextView)layInfView.findViewById(R.id.textView14_2)).setText(Integer.toString(tm.getTrophy(KEY_HYAKUMANGOKU)) + "回");
		}
		dialog.setTitle("獲得トロフィー");
		dialog.setContentView(layInfView);
		dialog.show();
	}
	public void onRipaiClick(View v) {
		tr.sortable = !this.ripaiCheck.isChecked();
		if(tr.sortable) {
			tr.sort();
		}
		else {
			tr.shuffle();
		}
		this.showTiles();
	}
	
	private class TileImageBuilder {
		
		private TileType currentType = TileType.man;
		
		public TileImageBuilder() {
			this(TileType.man);
		}

		public TileImageBuilder(TileType type) {
			setType(type);
		}
		
		public void setType(TileType type) {
			currentType = type;
		}
		
		public TileType getType() {
			return this.currentType;
		}
		
		public Drawable getTileImage(int num) {
			TypedArray images;
			if(currentType == TileType.pin) {
				images = getResources().obtainTypedArray(R.array.tileImagesPin);
			}
			else if(currentType == TileType.sou) {
				images = getResources().obtainTypedArray(R.array.tileImagesSou);
			}
			else {
				images = getResources().obtainTypedArray(R.array.tileImagesMan);	
			}
			
	    	return images.getDrawable(num - 1);
		}
		
		public Drawable getTileImageLarge(int num) {
			TypedArray images;
			if(currentType == TileType.pin) {
				images = getResources().obtainTypedArray(R.array.tileImagesPinLarge);
			}
			else if(currentType == TileType.sou) {
				images = getResources().obtainTypedArray(R.array.tileImagesSouLarge);
			}
			else {
				images = getResources().obtainTypedArray(R.array.tileImagesManLarge);	
			}
			
	    	return images.getDrawable(num - 1);
		}
	}
}