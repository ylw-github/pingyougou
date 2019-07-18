package com.pyg.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbUserMapper;
import com.pyg.pojo.TbUser;
import com.pyg.pojo.TbUserExample;
import com.pyg.pojo.TbUserExample.Criteria;
import com.pyg.user.service.UserService;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;

/**
 * 服务实现层
 * 
 * @author Administrator
 * 
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;

	// 注入redis模版
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbUser> findAll() {
		return userMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public PygResult add(TbUser user, String smsCode) {
		try {
			// 从redis中获取验证码
			Long number = (Long) redisTemplate.boundHashOps("smsCode").get(
					user.getPhone());
			String code = number+"";
			// 判断验证码是否正确
			if (!smsCode.equals(code)) {
				return new PygResult(false, "验证码错误");
			}
			// 密码加密
			String newPwd = DigestUtils.md5DigestAsHex(user.getPassword()
					.getBytes());
			// 设置到用户对象
			user.setPassword(newPwd);
			
			//创建时间
			Date date = new Date();
			user.setCreated(date);
			user.setUpdated(date);

			userMapper.insert(user);
			// 注册成功
			return new PygResult(true, "注册成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 注册失败
			return new PygResult(false, "注册失败");
		}
	}

	/**
	 * 修改
	 */
	@Override
	public void update(TbUser user) {
		userMapper.updateByPrimaryKey(user);
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbUser findOne(Long id) {
		return userMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			userMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbUser user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();

		if (user != null) {
			if (user.getUsername() != null && user.getUsername().length() > 0) {
				criteria.andUsernameLike("%" + user.getUsername() + "%");
			}
			if (user.getPassword() != null && user.getPassword().length() > 0) {
				criteria.andPasswordLike("%" + user.getPassword() + "%");
			}
			if (user.getPhone() != null && user.getPhone().length() > 0) {
				criteria.andPhoneLike("%" + user.getPhone() + "%");
			}
			if (user.getEmail() != null && user.getEmail().length() > 0) {
				criteria.andEmailLike("%" + user.getEmail() + "%");
			}
			if (user.getSourceType() != null
					&& user.getSourceType().length() > 0) {
				criteria.andSourceTypeLike("%" + user.getSourceType() + "%");
			}
			if (user.getNickName() != null && user.getNickName().length() > 0) {
				criteria.andNickNameLike("%" + user.getNickName() + "%");
			}
			if (user.getName() != null && user.getName().length() > 0) {
				criteria.andNameLike("%" + user.getName() + "%");
			}
			if (user.getStatus() != null && user.getStatus().length() > 0) {
				criteria.andStatusLike("%" + user.getStatus() + "%");
			}
			if (user.getHeadPic() != null && user.getHeadPic().length() > 0) {
				criteria.andHeadPicLike("%" + user.getHeadPic() + "%");
			}
			if (user.getQq() != null && user.getQq().length() > 0) {
				criteria.andQqLike("%" + user.getQq() + "%");
			}
			if (user.getIsMobileCheck() != null
					&& user.getIsMobileCheck().length() > 0) {
				criteria.andIsMobileCheckLike("%" + user.getIsMobileCheck()
						+ "%");
			}
			if (user.getIsEmailCheck() != null
					&& user.getIsEmailCheck().length() > 0) {
				criteria.andIsEmailCheckLike("%" + user.getIsEmailCheck() + "%");
			}
			if (user.getSex() != null && user.getSex().length() > 0) {
				criteria.andSexLike("%" + user.getSex() + "%");
			}

		}

		Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}
	
	//注入消息发送模版
	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * 需求:发送短信验证码
	 * @param smsCode
	 * @return
	 */
	public PygResult smsCode(String phone) {
		try {
			// 生成6位数验证码
			//0.11233243434
			long number = (long) (Math.random()*1000000);
			// 把6位数验证码保存在redis服务器
			redisTemplate.boundHashOps("smsCode").put(phone, number);
			
			//创建map对象
			Map<String, String> mapMessage = new HashMap<String, String>();
			mapMessage.put("mobile", phone);
			
			//设置签名
			mapMessage.put("signName", "黑马");
			
			//创建map
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", number+"");
			
			
			
			mapMessage.put("number", JSON.toJSONString(map));
			
			
			// 给短信发送网关服务发送消息 pyg-sms
			jmsTemplate.convertAndSend("smsQueue",JSON.toJSONString(mapMessage));
			
			//发送消息成功
			return new PygResult(true, "发送消息成功");
			
		} catch (JmsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return new PygResult(false, "发送消息失败");
		}
	}

}
