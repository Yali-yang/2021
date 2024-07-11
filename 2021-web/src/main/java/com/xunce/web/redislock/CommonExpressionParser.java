package com.xunce.web.redislock;

import com.xunce.web.exception.RedisBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author zhaozonglu001
 * @date 2023/6/19
 */
@Component
@Slf4j
public class CommonExpressionParser {
//	@Resource
//	private ExpressionParser expressionParser;
//
//	@Autowired
//	private ParameterNameDiscoverer parameterNameDiscoverer;

	public String getRealKey(String rawKey, ProceedingJoinPoint pjp) {
		return rawKey;
//		if (StringUtils.isEmpty(rawKey)) {
//			throw new RedisBusinessException("redis key can not be blank");
//		}
//
//		// 直接返回，不需要进行参数填充
//		/**
//		 * @RedisLock(key = "'payableBatch_'+#payableBatchId", errorMessage = "应付批次内存在需要变更的账单，正在处理，请刷新")
//		 *@RedisLock(key = "'doSeparate_'+#payable.recordNo", errorMessage = "分账正在处理中，请稍后再试")
//		 *@RedisLock(key = "'TgReceivableEntryAccountHandler_'" +
//		 *             "+#accountBo.bizOrderNo+#accountBo.version+#accountBo.bizCategory+#accountBo.transactionType",
//		 *             errorMessage = "入账正在处理中，请稍后再试")
//		 * #payableBatchId == 取入参payableBatchId字段，最终的结果是：payableBatch_1140
//		 */
//
//		Object[] args = pjp.getArgs();
//		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
//		EvaluationContext evaluationContext = new StandardEvaluationContext();
//		String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
//		for (int i = 0; i < parameterNames.length; i++) {
//			String paramName = parameterNames[i];
//			evaluationContext.setVariable(paramName, args[i]);
//		}
//		String realKey;
//		try {
//			realKey = (String) expressionParser.parseExpression(rawKey).getValue(evaluationContext);
//		} catch (SpelParseException e) {
//			realKey = rawKey;
//		}
//
//		if (StringUtils.isEmpty(realKey)) {
//			throw new RedisBusinessException("redis key can not be blank");
//		}
//		log.info("rawKey:{}, realKey:{}", rawKey, realKey);
//		return realKey;
	}
}
