package com.pyg.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;

@Configuration
public class PygConfiguration {
	
	//注入对象
	@Autowired
	private Environment env;
	
	@Bean
	public SolrTemplate getSolrTemplate(){
		//创建solrClient
		SolrClient solrClient = new HttpSolrClient(env.getProperty("spring.data.solr.host"));
		//创建对象
		SolrTemplate solrTemplate = new SolrTemplate(solrClient);
		
		return solrTemplate;
	}
	

}
