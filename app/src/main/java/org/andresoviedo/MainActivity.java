package org.andresoviedo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.andresoviedo.app.model3D.view.MenuActivity;
import org.andresoviedo.app.model3D.view.ModelActivity;
import org.andresoviedo.communication.Chat;
import org.andresoviedo.functions.Coin;
import org.andresoviedo.login.Greetings;
import org.andresoviedo.functions.Sleep;
import org.andresoviedo.functions.Sport;
import org.andresoviedo.functions.Study;
import org.andresoviedo.stabagotchi.Foods;
import org.andresoviedo.stabagotchi.Game;
import org.andresoviedo.stabagotchi.Pet;
import org.andresoviedo.util.android.AndroidURLStreamHandlerFactory;
import org.andresoviedo.getView3dOption;

import java.net.URL;

/**
 * This is the main android activity. From here we launch the whole stuff.
 *
 * Basically, this activity may serve to show a Splash screen and copy the assets (obj models) from the jar to external
 * directory.
 *
 * @author andresoviedo
 *
 */

// TODO: 2020/4/13
//  1. 登录界面应是主界面 2.功能的跳转有问题 3.狗的头上四个功能内在跳转逻辑整合的不够好


public class MainActivity extends Activity {


	View sleep,study,sport,user,friend,log_out;
	private Game game;
	private Pet pet;
	//  App layout
	private ProgressBar healthBar;
	private TextView level;
	private TextView coinPoints;
	private EditText name;
	private ImageView petImage;//为啥也没用到
	private Vibrator vibrator;
	private int dogImageFrameIndex;

	KeyListener storedKeylistener;
	Handler handler = new Handler();


	// Custom handler: org/andresoviedo/util/android/assets/Handler.class
	//以下是用来调取库的，误删！
	static {
		System.setProperty("java.protocol.handler.pkgs", "org.andresoviedo.util.android");
		URL.setURLStreamHandlerFactory(new AndroidURLStreamHandlerFactory());
	}


	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set main layout controls.
		// Basically, this is a screen with the app name just in the middle of the scree
		setContentView(R.layout.activity_game);//展示一下主界面

		sleep= (Button)findViewById(R.id.sleep);//我整的几个按钮
		study= (Button)findViewById(R.id.study);
		sport=(Button)findViewById(R.id.sport);
		user=(Button)findViewById(R.id.user);
		friend=(Button)findViewById(R.id.friend);
		log_out=(Button)findViewById(R.id.log_out);


		sleep.setOnClickListener(v -> {
			try{//跳到睡眠界面

				MainActivity.this.startActivity(new Intent(this, Sleep.class));
			}catch (Exception e) {
				throw e;
			}
		});
		study.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {//跳到学习逻辑
				MainActivity.this.startActivity(new Intent(MainActivity.this, Study.class));
			}
		});
		sport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {//跳到运动逻辑
				MainActivity.this.startActivity(new Intent(MainActivity.this, Sport.class));
			}
		});
		user.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {//跳到菜单？啥是菜单，哦3d的，放弃吧，用户
				MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), MenuActivity.class));
			}
		});
//		news.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), Chat.class));
//			}
//		});
		friend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {//聊天界面，莫得
				MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), Chat.class));
//				MainActivity.this.startActivity(new Intent(MainActivity.this, Chat.class));
//				MainActivity.this.finish();
//				Intent intent =new Intent(MainActivity.this, Chat.class);
//				startActivity(intent);
			}
		});
		log_out.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {//最最初始界面
				MainActivity.this.startActivity(new Intent(MainActivity.this, Greetings.class));
				MainActivity.this.finish();
			}
		});




		//      Create the pet object and the game object as well as the vibrating feedback variable
		pet = new Pet("Taco");//这只傻狗，内含命名，升级，健康与amore
		game = new Game(pet);//升级与喂食

		coinPoints = findViewById(R.id.coinPointsTextViewID);//金币系统，可以不看
		setCoinCount();

		vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
		//根据传入的NAME来取得对应的Object，然后转换成相应的服务对象
		//hhhhhhhhhhh，竟然还有震动，可可爱爱

		//      Start up the game
		healthBar = findViewById(R.id.healthBarID);
		level = findViewById(R.id.levelTextViewID);
		EditText name = findViewById(R.id.nameTextViewID);//显示一下名字
		petImage = findViewById(R.id.petImageID);//这只狗
		storedKeylistener = name.getKeyListener();//文本监听器


		//      Refresh screen and run the runnable code which is the health decreasing one per second
		refresh();//刷新屏幕
		handler.post(runnableCode);

		//      This starts the gif，开始疯狂抖动
		dogImageFrameIndex = 0;
		handler.postDelayed(updateDogImageFrameRunnable, 100);

		// 设置KeyListener为null, 变为不可输入状态//好的
		name.setKeyListener(null);
		// 如果需要,设置文字可选
		name.setTextIsSelectable(true);
		//可是你不编辑呀宝贝
		name.setOnLongClickListener(new View.OnLongClickListener() {
			final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			@Override
			public boolean onLongClick(View v) {
				// 可编辑时弹出软键盘
				imm.showSoftInput(name, 0);

				// 恢复KeyListener
				name.setKeyListener(storedKeylistener);
				// 如果需要,设置文字可选
				name.setTextIsSelectable(true);

				// 恢复KeyListener后,键盘不会自动弹出,要通过代码弹出
				name.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						imm.showSoftInput(name, 0);
					}
				});


				// 将光标定位到最后
				name.setSelection(name.getText().length());
				pet.setName(name.getText().toString());
				return false;
			}
		});



		petImage.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
//				getView3dOption.setView3D(true);
				Intent demoIntent = new Intent(MainActivity.this.getApplicationContext(), ModelActivity.class);
				demoIntent.putExtra("immersiveMode", "true");
				demoIntent.putExtra("backgroundColor", "0 0 0 1");
				MainActivity.this.startActivity(demoIntent);
				refresh();
				return false;
			}
		});

	}


	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	protected void onResume(){

		super.onResume();

		coinPoints = findViewById(R.id.coinPointsTextViewID);//金币系统，可以不看
		setCoinCount();
	}

	//是的，这是金币系统！
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public void setCoinCount() {
		SharedPreferences prefsd=getSharedPreferences("studyTime",MODE_PRIVATE);
		int studyCredit = prefsd.getInt("studyTime",0);
		SharedPreferences.Editor editorsd=getSharedPreferences("studyTime",MODE_PRIVATE).edit();
		editorsd.putInt("studyTime",0);
		editorsd.apply();

		SharedPreferences prefsl=getSharedPreferences("sleepTime",MODE_PRIVATE);
		int sleepCredit = prefsl.getInt("sleepTime",0);
		SharedPreferences.Editor editorsl=getSharedPreferences("sleepTime",MODE_PRIVATE).edit();
		editorsl.putInt("sleepTime",0);
		editorsl.apply();

		SharedPreferences prefsp=getSharedPreferences("walkDistance",MODE_PRIVATE);
		int sportCredit = prefsp.getInt("walkDistance",0);
		SharedPreferences.Editor editorsp=getSharedPreferences("walkDistance",MODE_PRIVATE).edit();
		editorsp.putInt("walkDistance",0);
		editorsp.apply();


		int coinnum=sleepCredit+studyCredit+sportCredit;

		SharedPreferences pref=getSharedPreferences("coinnum",MODE_PRIVATE);
		coinnum = coinnum+pref.getInt("CoinNumber",0);

		SharedPreferences.Editor editor=getSharedPreferences("coinnum",MODE_PRIVATE).edit();
		editor.putInt("CoinNumber",coinnum);
		editor.apply();

		/*String coinnums = String.valueOf(coinnum);*/
		pet.setCoinPoints(coinnum);
		/*coinPoints.setText(coinnums);*/
	}

	//This function opens the coin system.跳转到金币
	public void onCoinClicked(View view) {
		vibrator.vibrate(25);
		Intent intent =new Intent(MainActivity.this, Coin.class);
		startActivity(intent);
	}

	//  This code decreases the health by one every second.
	private Runnable runnableCode = new Runnable() {
		@Override
		public void run() {
			Log.d("Handlers", "Called on main thread");
			pet.decreaseHealthByOne();
			handler.postDelayed(runnableCode, 1000);
			if (pet.getHealthPoints() <= 0) {
				SharedPreferences.Editor editor=getSharedPreferences("coinnum",MODE_PRIVATE).edit();
				editor.putInt("CoinNumber",0);
				editor.apply();
			}
			refresh();
		}
	};

	//  This code cycles through the images to create the gif of Taco哈哈哈哈按帧抖动
	//Wow, you can really dance
	private Runnable updateDogImageFrameRunnable = new Runnable() {
		@Override
		public void run() {
			ImageView dogImageView = findViewById(R.id.petImageID);

			dogImageFrameIndex++;

			if (dogImageFrameIndex > 3) {
				dogImageFrameIndex = 0;
			}

			int dogImageFrameResourceId = getResources().getIdentifier("red_" + dogImageFrameIndex + "_pic", "drawable", getPackageName());
			dogImageView.setImageResource(dogImageFrameResourceId);

			handler.postDelayed(updateDogImageFrameRunnable, 100);
		}
	};

	//  This function refreshes the screen. It also includes the logic to make Taco evil at level 5.
	//  This is bad as it is bad practice to have logic in your Activity but due to the short timescale of the project it was implemented here.
	public void refresh() {
		String levelString = ""+pet.getLevel();
		level.setText(levelString);
		healthBar.setProgress(pet.getHealthPoints());
		String coinString = ""+pet.getCoinPoints();
		coinPoints.setText(coinString);
	}

	//This function adds the love point every time Taco is tapped. It also has evil Taco logic which is bad practice as explained above.
	public void onPetClicked(View view) {
		vibrator.vibrate(25);
		pet.addCoinPoint();
		refresh();
	}


	//  The code below controls the food feeding buttons, including what happens when you cannot afford the food.
	public void onFeedTreatClicked(View view) {
		if (pet.canAffordThisFood(Foods.TREAT.getCostOfFood())) {
			Toast.makeText(this, "A yummy treat for " + pet.getName(), Toast.LENGTH_SHORT).show();
			game.feed(Foods.TREAT);
		} else {
			Toast.makeText(this, "You can't afford this yet", Toast.LENGTH_SHORT).show();
		}
		refresh();
	}

	public void onFeedBowlClicked(View view) {
		if (pet.canAffordThisFood(Foods.BOWL.getCostOfFood())) {
			Toast.makeText(this, "A bowl of chow for " + pet.getName(), Toast.LENGTH_SHORT).show();
			game.feed(Foods.BOWL);
		} else {
			Toast.makeText(this, "You can't afford this yet", Toast.LENGTH_SHORT).show();
		}
		refresh();
	}

	public void onFeedBigBowlClicked(View view) {
		if (pet.canAffordThisFood(Foods.BIGBOWL.getCostOfFood())) {
			Toast.makeText(this, "A massive bowl of chow for " + pet.getName(), Toast.LENGTH_SHORT).show();
			game.feed(Foods.BIGBOWL);
		} else {
			Toast.makeText(this, "You can't afford this yet", Toast.LENGTH_SHORT).show();
		}
		refresh();
	}

	public void onFeedRibsClicked(View view) {
		if (pet.canAffordThisFood(Foods.RIBS.getCostOfFood())) {
			Toast.makeText(this, "Some tasty ribs for " + pet.getName(), Toast.LENGTH_SHORT).show();
			game.feed(Foods.RIBS);
		} else {
			Toast.makeText(this, "You can't afford this yet", Toast.LENGTH_SHORT).show();
		}
		refresh();
	}

	public void onFeedChickenClicked(View view) {
		if (pet.canAffordThisFood(Foods.CHICKEN.getCostOfFood())) {
			Toast.makeText(this, "A couple of chicken drumsticks for " + pet.getName(), Toast.LENGTH_SHORT).show();
			game.feed(Foods.CHICKEN);
		} else {
			Toast.makeText(this, "You can't afford this yet", Toast.LENGTH_SHORT).show();
		}
		refresh();
	}

	public void onFeedSteakClicked(View view) {
		if (pet.canAffordThisFood(Foods.STEAK.getCostOfFood())) {
			Toast.makeText(this, "A juicy steak for " + pet.getName(), Toast.LENGTH_SHORT).show();
			game.feed(Foods.STEAK);
		} else {
			Toast.makeText(this, "You can't afford this yet", Toast.LENGTH_SHORT).show();
		}
		refresh();
	}


	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public void onLevelUpClicked(View view) {
		if (pet.getCoinPoints() >= 100) {
			pet.levelUp();
			if (pet.getLevel() == 5){
				View backgroundLayout = findViewById(R.id.backgroundLayoutID);
				backgroundLayout.setBackground(getResources().getDrawable(R.drawable.backgrounddark));
				pet.setCoinPoints(0);
			}
			Toast.makeText(this, pet.getName() + " is now level " + pet.getLevel() + "!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "You need 100 coins to level up " + pet.getName(), Toast.LENGTH_SHORT).show();
		}

		refresh();
	}
/*	@SuppressWarnings("unused")
	private void init() {
		MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), ModelActivity.class));
		MainActivity.this.finish();
	}*/

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
}
