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
        
        //��vView�z��̐ݒ�
        setTehaiViewList();
        
        //��v�̏�����
        initTiles();
        
        
        
        /*
        final Dialog dialog = new Dialog(this);
		dialog.setTitle("Dialog");
		dialog.setContentView(R.layout.trophy);
		dialog.show();
		*/
		
    }
    
    /**
     * ��v�\��View�̏����ݒ�
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
     * ��v�\��
     */
    private void showTiles() {
    	for(int i = 1; i <= 13; i++) {
    		tehaiViewList.get(i - 1).setImageDrawable(getTileImage(tr.getTile(i)));
    	}
    }
    
    /**
     * �c���v�\��
     */
    private void showNextTile() {
    	tsumohaiView.setImageDrawable(getTileImage(tr.getNextTile()));
    	junme++;
    	showJunme();
    	clickedTilePos = 0;
    	clickedTileNumber = 0;
    }
    
    /**
     * �v��擾
     * @param num ����
     * @return �v��C���[�W
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
			Toast.makeText(this, "����", Toast.LENGTH_SHORT).show();
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
	 * �v��؂�
	 * @param i �؂�v�̈ʒu
	 */
	private void releaseTile(int i) {
		tr.releaseTile(i);
		showTiles();
		showNextTile();
	}
	
	/**
	 * �c���؂�
	 */
	private void tsumogiri() {
		showNextTile();
	}
	
	/**
	 * �a���{�^���N���b�N
	 * @param v
	 */
	public void onAgariClick(View v){
		
		if(endFlg) {
			return;
		}
		
		if(tr.isGoneOut()) {
			Toast.makeText(this, "���߂łƂ�", Toast.LENGTH_SHORT).show();
			countSuccess++;
			countWin++;
			checkTrophy();
		}
		else {
			Toast.makeText(this, "�`�����{", Toast.LENGTH_SHORT).show();
			countFailure++;
			countWin = 0;
		}
		endFlg = true;
		showResult();
		
	}
	
	/**
	 * �ŏ�����{�^���N���b�N
	 * @param v
	 */
	public void onResetClick(View v){
		initTiles();
	}
	
	/**
	 * ���ڕ\��
	 */
	private void showJunme() {
		TextView text = (TextView)findViewById(R.id.junmeText);
		//text.setText(Integer.toString(junme) + "����");
		text.setText("�c�� " + Integer.toString(MAX_JUNME - junme) + " ��");
	}
	
	/**
	 * ��v�̏�����
	 */
	private void initTiles() {
		tr = new TileRepository(!this.ripaiCheck.isChecked());
		
		junme = 0;
		
		endFlg = false;
		
		agariButton.setVisibility(View.VISIBLE);
		resetButton.setVisibility(View.INVISIBLE);
		
		//�z�v�̕\��
        showTiles();
        
        //��1����
        showNextTile();
	}
	
	/**
	 * ���ʂ̕\��
	 */
	private void showResult() {
		TextView text = (TextView)findViewById(R.id.resultText);
		text.setText("�����F" + Integer.toString(countSuccess) + "��@���s�F" + Integer.toString(countFailure) + "��");
		
		agariButton.setVisibility(View.INVISIBLE);
		resetButton.setVisibility(View.VISIBLE);
	}
	
	/**
	 * ���j���[�̒ǉ�
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
	          .setTitle("�v��I��")
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
			Toast.makeText(this, "3�A���̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_3RENSHO);
		}
		if(countWin == 5) {
			Toast.makeText(this, "5�A���̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_5RENSHO);
		}
		if(countWin == 10) {
			Toast.makeText(this, "10�A���̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_10RENSHO);
		}
		if(countWin == 20) {
			Toast.makeText(this, "20�A���̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_20RENSHO);
		}
		if(countWin == 30) {
			Toast.makeText(this, "30�A���̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_30RENSHO);
		}
		if(countWin == 40) {
			Toast.makeText(this, "40�A���̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_40RENSHO);
		}
		if(countWin == 50) {
			Toast.makeText(this, "50�A���̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_50RENSHO);
		}
		if(countWin == 100) {
			Toast.makeText(this, "100�A���̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_100RENSHO);
		}
		if(junme == 1) {
			Toast.makeText(this, "�V�a�̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_TENHO);
		}
		if(tr.isSuanko()) {
			Toast.makeText(this, "�l�Í��̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_SUANKO);
		}
		if(tr.isChuren()) {
			Toast.makeText(this, "��@�󓕂̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
			tm.setTrophy(KEY_CHUREN);
		}
		if(tb.getType() == TileType.pin) {
			if(tr.isDaisharin()) {
				Toast.makeText(this, "��ԗւ̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
				tm.setTrophy(KEY_DAISHARIN);
			}
		}
		if(tb.getType() == TileType.sou) {
			if(tr.isRyuiso()) {
				Toast.makeText(this, "�Έ�F�̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
				tm.setTrophy(KEY_RYUISO);
			}
		}
		if(tb.getType() == TileType.man) {
			if(tr.isHyakumangoku()) {
				Toast.makeText(this, "�S�ݐ΂̃g���t�B�[���l�����܂����I", Toast.LENGTH_SHORT).show();
				tm.setTrophy(KEY_HYAKUMANGOKU);
			}
		}
	}
	
	private void showTrophyDialog() {
		// ���C�A�E�g�C���t���[�^�[�g�p
        LayoutInflater factory = LayoutInflater.from(this);
        // ���̃��C�A�E�g�t�@�C�����w��
        View layInfView = factory.inflate(R.layout.trophy, null);
        
		final Dialog dialog = new Dialog(this);
		
		if(tm.getTrophy(KEY_3RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView1_1)).setText("�R�A��");
			((TextView)layInfView.findViewById(R.id.textView1_2)).setText(Integer.toString(tm.getTrophy(KEY_3RENSHO)) + "��");
		}
		if(tm.getTrophy(KEY_5RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView2_1)).setText("�T�A��");
			((TextView)layInfView.findViewById(R.id.textView2_2)).setText(Integer.toString(tm.getTrophy(KEY_5RENSHO)) + "��");
		}
		if(tm.getTrophy(KEY_10RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView3_1)).setText("�P�O�A��");
			((TextView)layInfView.findViewById(R.id.textView3_2)).setText(Integer.toString(tm.getTrophy(KEY_10RENSHO)) + "��");
		}
		if(tm.getTrophy(KEY_20RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView4_1)).setText("�Q�O�A��");
			((TextView)layInfView.findViewById(R.id.textView4_2)).setText(Integer.toString(tm.getTrophy(KEY_20RENSHO)) + "��");
		}
		if(tm.getTrophy(KEY_30RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView5_1)).setText("�R�O�A��");
			((TextView)layInfView.findViewById(R.id.textView5_2)).setText(Integer.toString(tm.getTrophy(KEY_30RENSHO)) + "��");
		}
		if(tm.getTrophy(KEY_40RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView6_1)).setText("�S�O�A��");
			((TextView)layInfView.findViewById(R.id.textView6_2)).setText(Integer.toString(tm.getTrophy(KEY_40RENSHO)) + "��");
		}
		if(tm.getTrophy(KEY_50RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView7_1)).setText("�T�O�A��");
			((TextView)layInfView.findViewById(R.id.textView7_2)).setText(Integer.toString(tm.getTrophy(KEY_50RENSHO)) + "��");
		}
		if(tm.getTrophy(KEY_100RENSHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView8_1)).setText("�P�O�O�A��");
			((TextView)layInfView.findViewById(R.id.textView8_2)).setText(Integer.toString(tm.getTrophy(KEY_100RENSHO)) + "��");
		}
		if(tm.getTrophy(KEY_TENHO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView9_1)).setText("�V�a");
			((TextView)layInfView.findViewById(R.id.textView9_2)).setText(Integer.toString(tm.getTrophy(KEY_TENHO)) + "��");
		}
		if(tm.getTrophy(KEY_SUANKO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView10_1)).setText("�l�Í�");
			((TextView)layInfView.findViewById(R.id.textView10_2)).setText(Integer.toString(tm.getTrophy(KEY_SUANKO)) + "��");
		}
		if(tm.getTrophy(KEY_CHUREN) > 0) {
			((TextView)layInfView.findViewById(R.id.textView11_1)).setText("��@��");
			((TextView)layInfView.findViewById(R.id.textView11_2)).setText(Integer.toString(tm.getTrophy(KEY_CHUREN)) + "��");
		}
		if(tm.getTrophy(KEY_DAISHARIN) > 0) {
			((TextView)layInfView.findViewById(R.id.textView12_1)).setText("��ԗ�");
			((TextView)layInfView.findViewById(R.id.textView12_2)).setText(Integer.toString(tm.getTrophy(KEY_DAISHARIN)) + "��");
		}
		if(tm.getTrophy(KEY_RYUISO) > 0) {
			((TextView)layInfView.findViewById(R.id.textView13_1)).setText("�Έ�F");
			((TextView)layInfView.findViewById(R.id.textView13_2)).setText(Integer.toString(tm.getTrophy(KEY_RYUISO)) + "��");
		}
		if(tm.getTrophy(KEY_HYAKUMANGOKU) > 0) {
			((TextView)layInfView.findViewById(R.id.textView14_1)).setText("�S�ݐ�");
			((TextView)layInfView.findViewById(R.id.textView14_2)).setText(Integer.toString(tm.getTrophy(KEY_HYAKUMANGOKU)) + "��");
		}
		dialog.setTitle("�l���g���t�B�[");
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