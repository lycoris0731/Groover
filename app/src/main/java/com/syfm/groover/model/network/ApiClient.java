package com.syfm.groover.model.network;

import android.text.format.DateFormat;
import android.util.Log;

import com.syfm.groover.model.Utils;
import com.syfm.groover.model.storage.Const;
import com.syfm.groover.model.storage.SharedPreferenceHelper;
import com.syfm.groover.model.storage.databases.AverageScore;
import com.syfm.groover.model.storage.databases.MusicData;
import com.syfm.groover.model.storage.databases.PlayerData;
import com.syfm.groover.model.storage.databases.ResultData;
import com.syfm.groover.model.storage.databases.ScoreRankData;
import com.syfm.groover.model.storage.databases.ShopSalesData;
import com.syfm.groover.model.storage.databases.StageData;
import com.syfm.groover.model.storage.databases.UserRank;

import org.jdeferred.android.AndroidDeferredManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmException;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by lycoris on 2015/09/27.
 */
public class ApiClient {
    private Realm realm;
    private AndroidDeferredManager deferred = new AndroidDeferredManager();

    /**
     * PlayData API
     */

    /**
     * Fetches player_data.php from mypage of Groove Coaster.
     *
     * @throws IOException
     * @throws JSONException
     */
    public void fetchPlayerData() throws IOException, JSONException {
        realm = Realm.getInstance(AppController.getContext());
        Log.d("ktr", "player data");
        String url = "https://mypage.groovecoaster.jp/sp/json/player_data.php";
        String player_data = "player_data";

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = AppController.getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            // TODO: エラー処理
            Log.d("ktr", "errordesu");
            return;
        }

        ResponseBody body = response.body();
        JSONObject object = new JSONObject(body.string()).getJSONObject(player_data);

        body.close();

        if (object.length() <= 0) {
            Log.d("ktr", "length0");
            return;
        }

        realm.beginTransaction();
        PlayerData playerData = realm.createObjectFromJson(PlayerData.class, object.toString());
        playerData.setDate(DateFormat.format("yyyy/MM/dd kk:mm:ss", Calendar.getInstance()).toString());
        realm.commitTransaction();
    }

    /**
     * Fetches shop_sales_data.php from mypage of Groove Coaster.
     *
     * @throws IOException
     * @throws JSONException
     */
    public void fetchShopSalesData() throws IOException, JSONException {
        realm = Realm.getInstance(AppController.getContext());
        Log.d("ktr", "shop data");
        String url = "https://mypage.groovecoaster.jp/sp/json/shop_sales_data.php";

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = AppController.getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            // TODO: エラー処理
            return;
        }
        ResponseBody body = response.body();
        JSONObject object = new JSONObject(body.string());

        body.close();

        if (object.length() <= 0) {
            return;
        }

        realm.beginTransaction();
        realm.createObjectFromJson(ShopSalesData.class, object);
        realm.commitTransaction();
    }

    /**
     * Fetches average_score.php from mypage of Groove Coaster.
     *
     * @throws IOException
     * @throws JSONException
     */
    public void fetchAverageScore() throws IOException, JSONException {
        realm = Realm.getInstance(AppController.getContext());
        Log.d("ktr", "ave data");

        String url = "https://mypage.groovecoaster.jp/sp/json/average_score.php";
        String average_data = "average";

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = AppController.getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            // TODO: エラー処理
            return;
        }

        ResponseBody body = response.body();

        JSONObject object = new JSONObject(body.string()).getJSONObject(average_data);

        body.close();

        if (object.length() <= 0) {
            return;
        }

        realm.beginTransaction();
        realm.createObjectFromJson(AverageScore.class, object.toString());
        realm.commitTransaction();
    }

    /**
     * Fetches stage_data.php from mypage of Groove Coaster.
     *
     * @throws IOException
     * @throws JSONException
     */
    public void fetchStageData() throws IOException, JSONException {
        realm = Realm.getInstance(AppController.getContext());
        Log.d("ktr", "stage data");

        String url = "https://mypage.groovecoaster.jp/sp/json/stage_data.php";
        String stage_data = "stage";

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = AppController.getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            // TODO: エラー処理
            return;
        }

        ResponseBody body = response.body();

        JSONObject object = new JSONObject(body.string()).getJSONObject(stage_data);

        body.close();

        if (object.length() <= 0) {
            return;
        }

        realm.beginTransaction();
        realm.createObjectFromJson(StageData.class, object.toString());
        realm.commitTransaction();
    }

    /**
     * MusicData API
     */

    /**
     * Fetches music_list.php from mypage of Groove Coaster.
     *
     * @throws IOException
     * @throws JSONException
     */
    public void fetchMusicData() throws IOException, JSONException {
        String url = "https://mypage.groovecoaster.jp/sp/json/music_list.php";
        String music_list_data = "music_list";

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = AppController.getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            // TODO: エラー処理
            return;
        }

        ResponseBody body = response.body();

        JSONArray array = new JSONObject(body.string()).getJSONArray(music_list_data);

        body.close();

        if (array.length() <= 0) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {
            // For DEBUG
            if (i > 3) {
                break;
            }

            Utils.sleep();

            realm = Realm.getInstance(AppController.getContext());
            realm.beginTransaction();

            try {
                MusicData musicData = fetchMusicDetail(array.getJSONObject(i), i, 3 - 1); //実際はlist.size() -1
                byte[] thumbnail = fetchMusicThumbnail(array.getJSONObject(i).getInt(Const.MUSIC_LIST_MUSIC_ID), musicData, i, 3 - 1);

                if (musicData == null || thumbnail == null) {
                    Log.d("ktr", "fetchMusicData Error");
                    realm.cancelTransaction();
                }

                musicData.setMusic_thumbnail(thumbnail);
                realm.commitTransaction();
            } catch (IOException e) {
                realm.cancelTransaction();
            } catch (JSONException e) {
                realm.cancelTransaction();
            } catch (RealmException e) {
                realm.cancelTransaction();
            }
        }
    }

    /**
     * Fetches music_detail.php from mypage of Groove Coaster.
     *
     * @param music A fetched list by fetchMusicData()
     * @param count The index of music list
     * @param size  A size of music list
     * @return {@code MusicData} if fetching was successfully, {@code null} if cannot fetches data.
     * @throws IOException
     * @throws JSONException
     */
    public MusicData fetchMusicDetail(final JSONObject music, final int count, final int size) throws IOException, JSONException {
        String url = "https://mypage.groovecoaster.jp/sp/json/music_detail.php?music_id=";
        String music_detail_data = "music_detail";
        String simple = "simple_result_data";
        String normal = "normal_result_data";
        String hard = "hard_result_data";
        String extra = "extra_result_data";
        String user_rank = "user_rank";
        String music_id = "music_id";

        url += music.getString(music_id);

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = AppController.getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            // TODO: エラー処理
            return null;
        }

        ResponseBody body = response.body();

        JSONObject object = new JSONObject(body.string()).getJSONObject(music_detail_data);

        body.close();

        if (object.length() <= 0) {
            return null;
        }

        resultDataJsonReplaceNull(object, simple);
        resultDataJsonReplaceNull(object, normal);
        resultDataJsonReplaceNull(object, hard);
        resultDataJsonReplaceNull(object, extra);

        userRankJsonReplaceNull(object.getJSONArray(user_rank));

        MusicData data = realm.createObjectFromJson(MusicData.class, object);
        data.setLast_play_time(music.getString(Const.MUSIC_LIST_LAST_PLAY_TIME));
        data.setPlay_count(music.getInt(Const.MUSIC_LIST_PLAY_COUNT));

        // 各難易度をMusicDataの子としてインサート
        // 要素にNULLがあると挙動がおかしくなるので気をつける

        ResultData resultSimple = realm.createObjectFromJson(ResultData.class, object.getJSONObject(simple).toString());
        data.getResult_data().add(resultSimple);

        ResultData resultNormal = realm.createObjectFromJson(ResultData.class, object.getJSONObject(normal).toString());
        data.getResult_data().add(resultNormal);

        ResultData resultHard = realm.createObjectFromJson(ResultData.class, object.getJSONObject(hard).toString());
        data.getResult_data().add(resultHard);

        ResultData resultExtra = realm.createObjectFromJson(ResultData.class, object.getJSONObject(extra).toString());
        data.getResult_data().add(resultExtra);

        // UserRankの整形
        JSONArray userRankRaw = object.getJSONArray(user_rank);

        for (int i = 0; i < userRankRaw.length(); i++) {
            UserRank userRank = realm.createObjectFromJson(UserRank.class, userRankRaw.get(i).toString());
            data.getUser_rank().add(userRank);
        }

        return data;
    }

    /**
     * Fetches music_image from mypage of Groove Coaster.
     *
     * @param id        The id of the music's
     * @param musicData Target music's data
     * @param count     The index of music list
     * @param maxSize   A size of music list
     * @return {@code byte[]} if fetching was successfully, {@code null} fetching was failed.
     * @throws IOException
     */
    public byte[] fetchMusicThumbnail(int id, final MusicData musicData, final int count, final int maxSize) throws IOException {
        String url = "https://mypage.groovecoaster.jp/sp/music/music_image.php?music_id=";
        url += id;

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = AppController.getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            // TODO: エラー処理
            Log.d("ktr", "thumb error");
            return null;
        }

        ResponseBody body = response.body();

        byte bytes[] = body.bytes();

        body.close();

        if (bytes.length <= 0) {
            Log.d("ktr", "thumb error");
            return null;
        }

        return bytes;
    }

    /**
     * Fetches score_ranking.php of the target music id.
     *
     * @param id   The target music's id
     * @param diff A difficulty of target music
     */
    public void fetchScoreRanking(final String id, final int diff) {
        final String url = "https://mypage.groovecoaster.jp/sp/json/score_ranking_bymusic_bydifficulty.php?music_id=" + id + "&difficulty=" + diff;

        realm = Realm.getInstance(AppController.getContext());

        String score_rank = "score_rank";

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = AppController.getOkHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                // TODO: エラー処理
                Log.d("ktr", "ApiClient.fetchScoreRanking response is not successful");
                return;
            }

            ResponseBody body = response.body();

            JSONArray array = new JSONObject(body.string()).getJSONArray(score_rank);

            body.close();

            if (array.length() <= 0) {
                return;
            }

            for (int i = 0; i < 5; i++) {
                realm.beginTransaction();
                ScoreRankData item = realm.createObjectFromJson(ScoreRankData.class, array.get(i).toString());
                item.setDiff(String.valueOf(diff));
                item.setId(id);
                realm.commitTransaction();
            }

        } catch (IOException e) {
            Log.d("ktr", e.toString());

        } catch (JSONException e) {
            Log.d("ktr", e.toString());
        }
    }

    /**
     * RankingData API
     */

    /**
     * Fetches ranking data from groovecoaster.jp
     *
     * @param RANKING_TYPE A type of fetches
     * @throws IOException
     */
    public void fetchRankingData(final String RANKING_TYPE) throws IOException {
        String url = "http://groovecoaster.jp/xml/fmj2100/rank/";

        switch (RANKING_TYPE) {
            case Const.SP_LEVEL_ALL_RANKING:
                url += "all/rank_1.xml";
                break;

            case Const.SP_LEVEL_SIMPLE_RANKING:
                url += "diff/0/rank_1.xml";
                break;

            case Const.SP_LEVEL_NORMAL_RANKING:
                url += "diff/1/rank_1.xml";
                break;

            case Const.SP_LEVEL_HARD_RANKING:
                url += "diff/2/rank_1.xml";
                break;

            case Const.SP_LEVEL_EXTRA_RANKING:
                url += "diff/3/rank_1.xml";
                break;

            case Const.SP_GENRE_JPOP_RANKING:
                url += "genre/J-POP/rank_1.xml";
                break;

            case Const.SP_GENRE_ANIME_RANKING:
                url += "genre/ANIME/rank_1.xml";
                break;

            case Const.SP_GENRE_VOCALOID_RANKING:
                url += "genre/VOCALOID/rank_1.xml";
                break;

            case Const.SP_GENRE_TOUHOU_RANKING:
                url += "genre/TOUHOU/rank_1.xml";
                break;

            case Const.SP_GENRE_GAME_RANKING:
                url += "genre/GAME/rank_1.xml";
                break;

            case Const.SP_GENRE_VARIETY_RANKING:
                url += "genre/VARIETY/rank_1.xml";
                break;

            case Const.SP_GENRE_ORIGINAL_RANKING:
                url += "genre/ORIGINAL/rank_1.xml";
                break;

            default:
                url += "all/rank_1.xml";
                break;
        }

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = AppController.getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            // TODO: エラー処理
            return;
        }

        ResponseBody body = response.body();
        String value = body.string();

        if (!value.isEmpty()) {
            SharedPreferenceHelper.setRankingData(RANKING_TYPE, value);
        }

        body.close();

    }

    /**
     * Fetches event ranking data from groovecoaster.jp
     *
     * @param SP_NAME The name of the SharedPreference for save this ranking data
     * @param number  The id of the target ranking
     * @throws IOException
     */
    public void fetchEventRankingData(final String SP_NAME, int number) throws IOException {
        String url = String.format("http://groovecoaster.jp/xml/fmj2100/rank/event/%03d/rank_1.xml", number + 1);

        Log.d("ktr", "url : " + url);

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = AppController.getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            // TODO: エラー処理
            return;
        }

        ResponseBody body = response.body();
        String value = body.string();

        Log.d("ktr", value);

        if (!value.isEmpty()) {
            SharedPreferenceHelper.setRankingData(SP_NAME, value);
        }

        body.close();
    }

    /**
     * Fetches event.xml to get a list of all event name.
     *
     * @throws IOException
     */
    public void fetchEventNameList() throws IOException {
        final String url = "http://groovecoaster.jp/xml/fmj2100/rank/event.xml";

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = AppController.getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            // TODO: エラー処理
            Log.d("ktr", "[RankingDataUseCase] fetchEventNameList OkHttp isNotSuccessful");
            return;
        }

        ResponseBody body = response.body();
        String value = body.string();

        if (!value.isEmpty()) {
            SharedPreferenceHelper.setEventNameList(value);
        }

        body.close();
    }

    /**
     * Other Methods
     */

    // TODO: すごく汚いから治したい

    /**
     * Replaces {@code null} to safety value.
     *
     * @param obj The target object
     * @param key A key of target object
     */
    private void resultDataJsonReplaceNull(JSONObject obj, String key) {
        if (obj.isNull(key) && obj.has(key)) {
            JSONObject result = new JSONObject();
            try {
                result.put(Const.MUSIC_RESULT_ADLIB, 0);
                result.put(Const.MUSIC_RESULT_FULL_CHAIN, 0);
                result.put(Const.MUSIC_RESULT_IS_CLEAR_MARK, "false");
                result.put(Const.MUSIC_RESULT_IS_FAILED_MARK, "false");
                result.put(Const.MUSIC_RESULT_MAX_CHAIN, 0);
                result.put(Const.MUSIC_RESULT_LEVEL, "-");
                result.put(Const.MUSIC_RESULT_NO_MISS, 0);
                result.put(Const.MUSIC_LIST_PLAY_COUNT, 0);
                result.put(Const.MUSIC_RESULT_RATING, "-");
                result.put(Const.MUSIC_RESULT_SCORE, 0);
                obj.put(key, result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    // TODO: すごく汚いから治したい

    /**
     * Replaces {@code null} to safety value.
     *
     * @param array The target array
     */
    private void userRankJsonReplaceNull(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            if (array.isNull(i)) {
                JSONObject rank = new JSONObject();
                try {
                    rank.put(Const.MUSIC_USER_RANK, 0);
                    array.put(i, rank);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
