package com.example.mpserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhanghd16 on 2018/5/21.
 */
@RestController
@RequestMapping("/schema")
public class SchemaController {

    @GetMapping("/")
    public String get(@RequestParam String mpName, @RequestParam String schemaName){
        //mpName：小程序的名字;schemaName获取哪个page的schema
        System.out.println("mpName:"+mpName+",schemaName:"+schemaName);
        //根据mpName和schemaName定位哪个小程序哪个page的配置
        String indexSchema = "{\"imgUrls\":[\"/static/images/tooopen_sy_143912755726.jpg\",\"/static/images/tooopen_sy_175866434296.jpg\",\"/static/images/tooopen_sy_175833047715.jpg\"],\"grids\":[{\"src\":\"/static/images/icon_nav_button.png\",\"name\":\"订单\",\"url\":\"/pages/log/log\"},{\"src\":\"/static/images/icon_nav_cell.png\",\"name\":\"一卡通\",\"url\":\"/pages/order/order\"},{\"src\":\"/static/images/icon_nav_panel.png\",\"name\":\"家园\",\"url\":\"/pages/order/order\"},{\"src\":\"/static/images/icon_nav_article.png\",\"name\":\"文章\",\"url\":\"/pages/order/order\"},{\"src\":\"/static/images/icon_nav_actionSheet.png\",\"name\":\"日历\",\"url\":\"/pages/order/order\"},{\"src\":\"/static/images/icon_nav_icons.png\",\"name\":\"记事本\",\"url\":\"/pages/order/order\"}],\"infos\":{\"name\":\"更多\",\"url\":\"/pages/log/log\",\"items\":[{\"title\":\"巨型球状天体1\",\"desc\":\"由各种物质组成的巨型球状天体，叫做星球。星球有一定的形状，有自己的运行轨道。\",\"from\":\"mpvue weui\",\"date\":\"2020/5/20 17:03\",\"extra\":\"其它\",\"src\":\"/static/images/icon_nav_article.png\",\"url\":\"/pages/log/log\"},{\"title\":\"巨型球状天体2\",\"desc\":\"由各种物质组成的巨型球状天体，叫做星球。星球有一定的形状，有自己的运行轨道。\",\"from\":\"mpvue weui\",\"date\":\"2018/5/14 17:03\",\"extra\":\"其它\",\"src\":\"/static/images/icon_nav_article.png\",\"url\":\"/pages/log/log\"},{\"title\":\"巨型球状天体3\",\"desc\":\"由各种物质组成的巨型球状天体，叫做星球。星球有一定的形状，有自己的运行轨道。\",\"from\":\"mpvue weui\",\"date\":\"2018/5/12 17:03\",\"extra\":\"其它\",\"src\":\"/static/images/icon_nav_article.png\",\"url\":\"/pages/log/log\"}]},\"footer\":{\"copyright\":\"Copyright © 2008-2018 weui.io\",\"links\":[{\"name\":\"mpvue weui\",\"url\":\"/pages/order/order\"}]},\"tabs\":[{\"title\":\"标题1\",\"content\":\"标题1的内容\"},{\"title\":\"标题2\",\"content\":\"标题2的内容\"},{\"title\":\"标题3\",\"content\":\"标题3的内容\"}]}";
        return indexSchema;
    }
}
