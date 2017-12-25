package org.lpw.ranch.transfer;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.TimeUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
public class CompleteTest extends TestSupport {
    @Test
    public void complete() {
        mockHelper.reset();
        mockHelper.mock("/transfer/complete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3011, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(TransferModel.NAME + ".orderNo")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3007, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-greater-than", message.get(TransferModel.NAME + ".amount"), 0), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "-1");
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3007, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-greater-than", message.get(TransferModel.NAME + ".amount"), 0), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3012, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(TransferModel.NAME + ".tradeNo")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("tradeNo", generator.random(101));
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3013, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(TransferModel.NAME + ".tradeNo"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3014, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(TransferModel.NAME + ".state"), 1, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.getRequest().addParameter("state", "3");
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3014, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(TransferModel.NAME + ".state"), 1, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.getRequest().addParameter("state", "1");
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        JSONObject notice = new JSONObject();
        notice.put("http", "notice 1");
        TransferModel transfer1 = create(1, 0, notice.toJSONString());
        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.getRequest().addParameter("state", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3015, object.getIntValue("code"));
        Assert.assertEquals(message.get(TransferModel.NAME + ".not-exists"), object.getString("message"));

        httpAspect.reset();
        List<String> urls = new ArrayList<>();
        List<Map<String, String>> headers = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        List<String> contents = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            contents.add("content " + i);
        httpAspect.post(urls, headers, parameters, contents);

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", transfer1.getId());
        mockHelper.getRequest().addParameter("amount", "2");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.getRequest().addParameter("state", "1");
        mockHelper.getRequest().addParameter("label", "label 1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(12, data.size());
        Assert.assertEquals("type 1", data.getString("type"));
        Assert.assertEquals("user 1", data.getString("user"));
        Assert.assertEquals("account 1", data.getString("account"));
        Assert.assertEquals(2, data.getIntValue("amount"));
        Assert.assertEquals("order no 1", data.getString("orderNo"));
        Assert.assertEquals("bill no 1", data.getString("billNo"));
        Assert.assertEquals("trade no", data.getString("tradeNo"));
        Assert.assertEquals(1, data.getIntValue("state"));
        Assert.assertEquals(notice.toJSONString(), data.getString("notice"));
        long time = dateTime.toTime(data.getString("start")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < TimeUnit.Hour.getTime() + 2000L);
        time = dateTime.toTime(data.getString("end")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        TransferModel transfer11 = liteOrm.findById(TransferModel.class, transfer1.getId());
        Assert.assertEquals("type 1", transfer11.getType());
        Assert.assertEquals("user 1", transfer11.getUser());
        Assert.assertEquals("account 1", transfer11.getAccount());
        Assert.assertEquals(2, transfer11.getAmount());
        Assert.assertEquals("order no 1", transfer11.getOrderNo());
        Assert.assertEquals("bill no 1", transfer11.getBillNo());
        Assert.assertEquals("trade no", transfer11.getTradeNo());
        Assert.assertEquals(1, transfer11.getState());
        Assert.assertEquals(notice.toJSONString(), transfer11.getNotice());
        time = transfer11.getStart().getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < TimeUnit.Hour.getTime() + 2000L);
        Assert.assertTrue(System.currentTimeMillis() - transfer11.getEnd().getTime() < 2000L);
        JSONObject json = this.json.toObject(transfer11.getJson());
        Assert.assertEquals(2, json.size());
        Assert.assertEquals("label 1", json.getString("label"));
        Assert.assertEquals("{\"label\":\"label 1\"}", json.getJSONObject("complete").toJSONString());
        Assert.assertEquals(1, urls.size());
        Assert.assertEquals("notice 1", urls.get(0));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", transfer1.getOrderNo());
        mockHelper.getRequest().addParameter("amount", "3");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.getRequest().addParameter("state", "2");
        mockHelper.getRequest().addParameter("label", "label 2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(12, data.size());
        Assert.assertEquals("type 1", data.getString("type"));
        Assert.assertEquals("user 1", data.getString("user"));
        Assert.assertEquals("account 1", data.getString("account"));
        Assert.assertEquals(2, data.getIntValue("amount"));
        Assert.assertEquals("order no 1", data.getString("orderNo"));
        Assert.assertEquals("bill no 1", data.getString("billNo"));
        Assert.assertEquals("trade no", data.getString("tradeNo"));
        Assert.assertEquals(1, data.getIntValue("state"));
        Assert.assertEquals(notice.toJSONString(), data.getString("notice"));
        time = dateTime.toTime(data.getString("start")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < TimeUnit.Hour.getTime() + 2000L);
        time = dateTime.toTime(data.getString("end")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        TransferModel transfer111 = liteOrm.findById(TransferModel.class, transfer1.getId());
        Assert.assertEquals("type 1", transfer111.getType());
        Assert.assertEquals("user 1", transfer111.getUser());
        Assert.assertEquals("account 1", transfer111.getAccount());
        Assert.assertEquals(2, transfer111.getAmount());
        Assert.assertEquals("order no 1", transfer111.getOrderNo());
        Assert.assertEquals("bill no 1", transfer111.getBillNo());
        Assert.assertEquals("trade no", transfer11.getTradeNo());
        Assert.assertEquals(1, transfer111.getState());
        Assert.assertEquals(notice.toJSONString(), transfer111.getNotice());
        time = transfer111.getStart().getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < TimeUnit.Hour.getTime() + 2000L);
        Assert.assertTrue(System.currentTimeMillis() - transfer111.getEnd().getTime() < 2000L);
        json = this.json.toObject(transfer11.getJson());
        Assert.assertEquals(2, json.size());
        Assert.assertEquals("label 1", json.getString("label"));
        Assert.assertEquals("{\"label\":\"label 1\"}", json.getJSONObject("complete").toJSONString());
        Assert.assertEquals(1, urls.size());

        notice.put("http", "notice 2");
        TransferModel transfer2 = create(2, 0, notice.toJSONString());
        urls.clear();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", transfer2.getId());
        mockHelper.getRequest().addParameter("amount", "2");
        mockHelper.getRequest().addParameter("tradeNo", "trade no 2");
        mockHelper.getRequest().addParameter("state", "2");
        mockHelper.getRequest().addParameter("label", "label 2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/transfer/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(12, data.size());
        Assert.assertEquals("type 2", data.getString("type"));
        Assert.assertEquals("user 2", data.getString("user"));
        Assert.assertEquals("account 2", data.getString("account"));
        Assert.assertEquals(2, data.getIntValue("amount"));
        Assert.assertEquals("order no 2", data.getString("orderNo"));
        Assert.assertEquals("bill no 2", data.getString("billNo"));
        Assert.assertEquals("trade no 2", data.getString("tradeNo"));
        Assert.assertEquals(2, data.getIntValue("state"));
        Assert.assertEquals(notice.toJSONString(), data.getString("notice"));
        time = dateTime.toTime(data.getString("start")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > 2 * TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < 2 * TimeUnit.Hour.getTime() + 2000L);
        time = dateTime.toTime(data.getString("end")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        TransferModel transfer22 = liteOrm.findById(TransferModel.class, transfer2.getId());
        Assert.assertEquals("type 2", transfer22.getType());
        Assert.assertEquals("user 2", transfer22.getUser());
        Assert.assertEquals("account 2", transfer22.getAccount());
        Assert.assertEquals(2, transfer22.getAmount());
        Assert.assertEquals("order no 2", transfer22.getOrderNo());
        Assert.assertEquals("bill no 2", transfer22.getBillNo());
        Assert.assertEquals("trade no 2", transfer22.getTradeNo());
        Assert.assertEquals(2, transfer22.getState());
        Assert.assertEquals(notice.toJSONString(), transfer22.getNotice());
        time = transfer22.getStart().getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > 2 * TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < 2 * TimeUnit.Hour.getTime() + 2000L);
        Assert.assertTrue(System.currentTimeMillis() - transfer11.getEnd().getTime() < 2000L);
        json = this.json.toObject(transfer22.getJson());
        Assert.assertEquals(2, json.size());
        Assert.assertEquals("label 2", json.getString("label"));
        Assert.assertEquals("{\"label\":\"label 2\"}", json.getJSONObject("complete").toJSONString());
        Assert.assertEquals(1, urls.size());
        Assert.assertEquals("notice 2", urls.get(0));
    }
}
