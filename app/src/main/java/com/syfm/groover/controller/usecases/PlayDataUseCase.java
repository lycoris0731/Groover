package com.syfm.groover.controller.usecases;

import android.util.Log;
import android.widget.Toast;

import com.syfm.groover.model.Utils;
import com.syfm.groover.model.network.ApiClient;
import com.syfm.groover.model.network.AppController;
import com.syfm.groover.model.storage.Const;
import com.syfm.groover.model.storage.databases.AverageScore;
import com.syfm.groover.model.storage.databases.PlayerData;
import com.syfm.groover.model.storage.databases.ShopSalesData;
import com.syfm.groover.model.storage.databases.StageData;

import org.jdeferred.Promise;
import org.jdeferred.android.AndroidDeferredManager;
import org.json.JSONException;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import io.realm.Realm;

/**
 * Created by lycoris on 2015/09/26.
 */
public class PlayDataUseCase {

    private AndroidDeferredManager deferred = new AndroidDeferredManager();

    // PlayDataを通知するためのクラス
    // PlayDataFragmentへ通知
    public class PlayDataEvent {
        public final PlayerData playerData;
        public final ShopSalesData salesData;
        public final AverageScore averageScore;
        public final StageData stageData;
        public PlayDataEvent(PlayerData playerData, ShopSalesData salesData, AverageScore averageScore, StageData stageData) {
            this.playerData   = playerData;
            this.salesData    = salesData;
            this.averageScore = averageScore;
            this.stageData    = stageData;
        }
    }

    // ログイン判定用のクラス
    // LoginActivityへ通知
    public class SetPlayData {
        public final boolean success;
        public final String  message;
        public SetPlayData(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    public void setPlayData() {
        ApiClient client = new ApiClient();

        // なぜかnetworkOnMainThreadException
        Promise p = deferred.when(() -> {
            try {
                client.fetchPlayerData();
                client.fetchShopSalesData();
                client.fetchAverageScore();
                client.fetchStageData();
            } catch (IOException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new SetPlayData(false, "プレイデータの取得に失敗しました。通信環境の良い場所で再取得して下さい。"));
            } catch (JSONException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new SetPlayData(false, "JSONデータのパースに失敗しました。取得したデータが不正です。"));
            }
        }).done(callback -> {
            Log.d("ktr", "setPlayDataDone");
            EventBus.getDefault().post(new SetPlayData(true, null));
        }).fail(e -> {
            e.printStackTrace();
            EventBus.getDefault().post(new SetPlayData(false, e.getMessage()));
        });
    }

    public void getPlayData() {
        Realm realm = Realm.getInstance(AppController.getInstance());
        PlayerData player    = realm.where(PlayerData.class).findFirst();
        ShopSalesData sales  = realm.where(ShopSalesData.class).findFirst();
        AverageScore average = realm.where(AverageScore.class).findFirst();
        StageData stage      = realm.where(StageData.class).findFirst();

        EventBus.getDefault().post(new PlayDataEvent(player, sales, average, stage));
    }
}
