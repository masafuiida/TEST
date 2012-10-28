package team.asaichi.oneColorMJ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class TileRepository {
	private int[] restTileArray = new int[36];
	private int restTileCount = 0;
	private ArrayList<Integer> handTileList = new ArrayList<Integer>();
	public int nextTile;
	public boolean sortable = true; 
	/**
	 * コンストラクタ
	 */
	public TileRepository(boolean sortable) {
		this.sortable = sortable;
		for(int i = 1; i <= 36; i++) {
			restTileArray[i - 1] = 0;
		}
		restTileCount = 36;
		for(int i = 1; i <= 13; i++) {
			AddHandTile(pick());
		}
		
		/*
		handTileList.add(7);
		handTileList.add(7);
		handTileList.add(7);
		handTileList.add(7);
		handTileList.add(8);
		handTileList.add(8);
		handTileList.add(8);
		handTileList.add(8);
		handTileList.add(9);
		handTileList.add(9);
		handTileList.add(9);
		handTileList.add(9);
		handTileList.add(1);
		*/
	}
	
	public TileRepository() {
		this(true);
	}
	/**
	 * 手牌の取得(数字)
	 * @param i 牌の位置
	 * @return 牌の数字
	 */
	public int getTile(int i){
		return handTileList.get(i - 1);
	}
	
	/**
	 * 牌山残り判定
	 * @return
	 */
	public boolean hasNextTile() {
		return restTileCount > 0;
	}
	
	/**
	 * 次のツモ
	 * @return 次のツモの数字
	 */
	public int getNextTile() {
		nextTile = pick();
		//nextTile = 1;
		return nextTile;
	}
	
	/**
	 * 牌を切る
	 * 指定の牌を切り、ツモ牌を手牌に組み入れる
	 * @param i 切る牌の位置
	 */
	public void releaseTile(int i) {
		handTileList.remove(i - 1);
		AddHandTile(nextTile);
	}
	
	/**
	 * ツモる
	 * 牌山から１枚ツモる
	 * @return ツモった牌の数字
	 */
	private int pick() {
		int i ;
		int returnNumber = -1;
		Random rnd = new Random();
		while (true) {
			if(restTileCount == 0) {
				break;
			}
			i = rnd.nextInt(36);
			if(restTileArray[i] == 0) {
				restTileArray[i] = 1;
				returnNumber = i / 4 + 1;
				restTileCount--;
				break;
			}
			
		}
		return returnNumber;
	}
	
	/**
	 * 手牌追加
	 * @param num 数字
	 */
	private void AddHandTile(int num) {
		handTileList.add(num);
		if(this.sortable) {
			Collections.sort(handTileList);
		}
	}
	
	/**
	 * 和了判定
	 * @return true:アガリ false:
	 */
	public boolean isGoneOut() {
		ArrayList<Integer> al = (ArrayList<Integer>) handTileList.clone();
		al.add(nextTile);
		Collections.sort(al);
		int[] n = new int[9];
		Arrays.fill(n, 0);
		int[] zn = n.clone();
		
		for(int i : al) {
			n[i - 1]++;
		}
		
		for(int i = 0; i < 9; i++) {
			int[] wk = n.clone();
			if(wk[i] >= 2) {
				wk[i] -= 2;
				int[] wk1 = wk.clone();
				int[] wk2 = wk.clone();
				for(int j = 0; j < 2; j++) {
					if(j == 0) {
						//刻子判定
						for(int k = 0; k < 9; k++) {
							if(wk1[k] >= 3) {
								wk1[k] -= 3;
							}
						}
						
						//順子判定
						for(int k = 0; k < 7;) {
							if(wk1[k] > 0 && wk1[k + 1] > 0 && wk1[k + 2] > 0) {
								wk1[k]--;
								wk1[k + 1]--;
								wk1[k + 2]--;
							}
							else {
								k++;
							}
						}
						if (Arrays.equals(wk1,zn)) {
							return true;
						}
					}
					else {
						//順子判定
						for(int k = 0; k < 7;) {
							if(wk2[k] > 0 && wk2[k + 1] > 0 && wk2[k + 2] > 0) {
								wk2[k]--;
								wk2[k + 1]--;
								wk2[k + 2]--;
							}
							else {
								k++;
							}
						}
						
						//刻子判定
						for(int k = 0; k < 9; k++) {
							if(wk2[k] >= 3) {
								wk2[k] -= 3;
							}
						}
						if (Arrays.equals(wk2,zn)) {
							return true;
						}
					}
				}
			}
			
			
		}
		
		if(isSevenPairs(n)) {
			return true;
		}
		
		return false;
	}

	/**
	 * 七対子判定
	 * @param n
	 * @return
	 */
	private boolean isSevenPairs(int[] n) {
		//チートイ判定
		for(int i = 0; i < 9; i++) {
			if(n[i] != 0 && n[i] != 2) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isSuanko() {
		ArrayList<Integer> al = (ArrayList<Integer>) handTileList.clone();
		al.add(nextTile);
		Collections.sort(al);
		int[] n = new int[9];
		Arrays.fill(n, 0);
		
		for(int i : al) {
			n[i - 1]++;
		}
		
		boolean knownHead = false;
		for(int i = 0; i < 9; i++) {
			if(n[i] == 2) {
				if(knownHead) {
					return false;
				}
				else {
					knownHead = true;
				}
			}
			else {
				if(n[i] != 3 && n[i] > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isChuren() {
		ArrayList<Integer> al = (ArrayList<Integer>) handTileList.clone();
		al.add(nextTile);
		Collections.sort(al);
		int[] n = new int[9];
		Arrays.fill(n, 0);
		
		for(int i : al) {
			n[i - 1]++;
		}
		
		if(n[0] > 2
		&& n[1] > 0
		&& n[2] > 0
		&& n[3] > 0
		&& n[4] > 0
		&& n[5] > 0
		&& n[6] > 0
		&& n[7] > 0
		&& n[8] > 2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isDaisharin() {
		ArrayList<Integer> al = (ArrayList<Integer>) handTileList.clone();
		al.add(nextTile);
		Collections.sort(al);
		int[] n = new int[9];
		Arrays.fill(n, 0);
		
		for(int i : al) {
			n[i - 1]++;
		}
		
		if(n[1] == 2
		&& n[2] == 2
		&& n[3] == 2
		&& n[4] == 2
		&& n[5] == 2
		&& n[6] == 2
		&& n[7] == 2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isRyuiso() {
		ArrayList<Integer> al = (ArrayList<Integer>) handTileList.clone();
		al.add(nextTile);
		Collections.sort(al);
		int[] n = new int[9];
		Arrays.fill(n, 0);
		
		for(int i : al) {
			n[i - 1]++;
		}
		
		if(n[0] == 0
		&& n[1] > 0
		&& n[2] > 0
		&& n[3] > 0
		&& n[4] == 0
		&& n[5] > 0
		&& n[6] == 0
		&& n[7] > 0
		&& n[8] == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isHyakumangoku() {
		ArrayList<Integer> al = (ArrayList<Integer>) handTileList.clone();
		al.add(nextTile);
		Collections.sort(al);
		int sum = 0;
		for(int i : al) {
			sum += i;
		}
		
		if(sum >= 100) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void sort() {
		Collections.sort(handTileList);
	}
	
	public void shuffle() {
		Collections.shuffle(handTileList);
	}
}
