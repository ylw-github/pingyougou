package com.pyg.utils;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.activemq.filter.function.makeListFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pyg.pojo.TbItem;

@Component
public class SolrHtmlUtils {

	// 注入solrTempldate
	@Autowired
	private SolrTemplate solrTemplate;

	/**
	 * 需求:接受消息,同步索引库 业务: 1,接受发布订阅空间消息 2,使用solr服务把消息写入索引库即可
	 */
	@JmsListener(destination = "solrTopic")
	public void readeMessage(String message) {
		
		System.out.println("接受消息.........................");
		
		// 判断传递的值是否为空
		if (StringUtils.isNotBlank(message)) {			
			// 截取
			String StrJson = message.substring(message.indexOf(":")+1);
			
			if (message.contains("add")) {
				
				// 获取id
				// String ids = (String) messageMap.get("dele");
				// 把itemJson转换成集合
				List<TbItem> list = JSON.parseArray(StrJson, TbItem.class);
				// 删除
				solrTemplate.saveBeans(list);
				// 提交
				solrTemplate.commit();
			} else {

				// 获取id
				// String ids = (String) messageMap.get("dele");
				// 把ids转换成集合
				List<String> list = JSON.parseArray(StrJson, String.class);
				// 删除
				solrTemplate.deleteById(list);
				// 提交
				solrTemplate.commit();

			}

		}

	}
	
	@JmsListener(destination="mytopic")
	public void demo(String message){
		System.out.println("接受消息:"+message);
	}

}
