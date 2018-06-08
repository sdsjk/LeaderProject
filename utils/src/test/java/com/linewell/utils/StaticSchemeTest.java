package com.linewell.utils;

import com.github.yoojia.inputs.NextInputs;
import com.github.yoojia.inputs.Provider;
import com.github.yoojia.inputs.StaticScheme;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by caicai on 2016-07-14.
 */
public class StaticSchemeTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void schemeTest(){
        NextInputs inputs = new NextInputs();
        inputs.add(Provider.fromString("yoojia")).with(StaticScheme.Required())

                .add(Provider.fromString("yoojia.chen@gmail.com"))
                .with(StaticScheme.Email())

                .add(Provider.fromString("13800138000"))
                .with(StaticScheme.ChineseIDCard())

                .add(Provider.fromString("4121551474702170"))
                .with(StaticScheme.BlankCard());

        if(inputs.test()) {
            // Passed
        }

//StaticScheme
//        Required - 必填模式
//        NotBlank - 非空模式
//        Digits - 数字模式
//        Email - 邮件地址模式
//        IPv4 - IP地址模式
//        Host - 域名模式
//        URL - URL地址模式
//        Numeric - 数值模式
//        BlankCard - 银行卡/信用卡号码模式
//        ChineseIDCard 身份证号码模式
//        ChineseMobile 手机号码模式（国内手机号）
//        IsTrue - 结果为True模式
//        IsFalse - 结果为False模式



//        ValueScheme - 数值校验模式
//
//        数值校验模式需要指定校验参数来完成校验。 NextInputs目前内置包含以下几种数值校验模式，在未来版本也会加入其它使用频率较高的模式：
//
//        Required - 必填模式，与静态校验模式的必填模式相同。
//        MinLength - 最小内容长度
//        MaxLength - 最多内容长度
//        RangeLength - 内容长度在指定范围内
//        MinValue - 最小值
//        MaxValue - 最大值
//        RangeValue - 数值范围
//        Equals - 与指定内容相同
//        NotEquals - 与指定内容不相同



//        设置校验失败提示消息
//
//        NextInputs内置的所有校验模式都自带提示消息，这些提示消息描述也比较“抽象”，很可能不符合你的业务需要。使用 .msgOnFail(String) 接口或者 .msg(String) 接口可以设置校验失败提示消息。当校验失败时，提示消息将通过MessageDisplay接口的具体实现类处理并显示出来。
//
//        这两个接口的作用是相同的，如果你觉得.msg(String)会导致你突然忘记这方法是干哈的，你可以使用.msgOnFail(String)方法来提醒自己。
//
//        使用 NextInputs.setMessageDisplay(MessageDisplay) 方法可以覆盖默认实现，使用你想要的校验失败提示方式。
    }
}
