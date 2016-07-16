package org.apache.ibatis.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.FifoCache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.decorators.SoftCache;
import org.apache.ibatis.cache.decorators.WeakCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ReuseExecutor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.FastResultSetHandler;
import org.apache.ibatis.executor.resultset.NestedResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.apache.ibatis.type.TypeHandlerRegistry;

public class Configuration
{

	protected Environment environment;

	protected boolean lazyLoadingEnabled = false;
	protected boolean aggressiveLazyLoading = true;
	protected boolean multipleResultSetsEnabled = true;
	protected boolean useGeneratedKeys = false;
	protected boolean useColumnLabel = true;
	protected boolean cacheEnabled = true;
	protected Integer defaultStatementTimeout;
	protected ExecutorType defaultExecutorType = ExecutorType.SIMPLE;
	protected AutoMappingBehavior autoMappingBehavior = AutoMappingBehavior.PARTIAL;

	protected Properties variables = new Properties();
	protected ObjectFactory objectFactory = new DefaultObjectFactory();
	protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
	protected MapperRegistry mapperRegistry = new MapperRegistry(this);

	protected final InterceptorChain interceptorChain = new InterceptorChain();
	protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
	protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
	protected final Map<String, MappedStatement> mappedStatements = new StrictMap<String, MappedStatement>(
			"Mapped Statements collection");
	protected final Map<String, Cache> caches = new StrictMap<String, Cache>("Caches collection");
	protected final Map<String, ResultMap> resultMaps = new StrictMap<String, ResultMap>("Result Maps collection");
	protected final Map<String, ParameterMap> parameterMaps = new StrictMap<String, ParameterMap>(
			"Parameter Maps collection");
	protected final Map<String, KeyGenerator> keyGenerators = new StrictMap<String, KeyGenerator>(
			"Key Generators collection");

	protected final Set<String> loadedResources = new HashSet<String>();
	protected final Map<String, XNode> sqlFragments = new StrictMap<String, XNode>(
			"XML fragments parsed from previous mappers");

	protected final Collection<XMLStatementBuilder> incompleteStatements = new LinkedList<XMLStatementBuilder>();
	protected final Collection<CacheRefResolver> incompleteCacheRefs = new LinkedList<CacheRefResolver>();
	/**
	 * A map holds cache-ref relationship. The key is the namespace that
	 * references a cache bound to another namespace and the value is the
	 * namespace which the actual cache is bound to.
	 */
	protected final Map<String, String> cacheRefMap = new HashMap<String, String>();

	public Configuration(Environment environment)
	{
		this();
		this.environment = environment;
	}

	public Configuration()
	{
		typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class.getName());
		typeAliasRegistry.registerAlias("MANAGED", ManagedTransactionFactory.class.getName());
		typeAliasRegistry.registerAlias("JNDI", JndiDataSourceFactory.class.getName());
		typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class.getName());
		typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class.getName());

		typeAliasRegistry.registerAlias("PERPETUAL", PerpetualCache.class.getName());
		typeAliasRegistry.registerAlias("FIFO", FifoCache.class.getName());
		typeAliasRegistry.registerAlias("LRU", LruCache.class.getName());
		typeAliasRegistry.registerAlias("SOFT", SoftCache.class.getName());
		typeAliasRegistry.registerAlias("WEAK", WeakCache.class.getName());
	}

	public void addLoadedResource(String resource)
	{
		loadedResources.add(resource);
	}

	public boolean isResourceLoaded(String resource)
	{
		return loadedResources.contains(resource);
	}

	public Environment getEnvironment()
	{
		return environment;
	}

	public void setEnvironment(Environment environment)
	{
		this.environment = environment;
	}

	public AutoMappingBehavior getAutoMappingBehavior()
	{
		return autoMappingBehavior;
	}

	public void setAutoMappingBehavior(AutoMappingBehavior autoMappingBehavior)
	{
		this.autoMappingBehavior = autoMappingBehavior;
	}

	public boolean isLazyLoadingEnabled()
	{
		return lazyLoadingEnabled;
	}

	public void setLazyLoadingEnabled(boolean lazyLoadingEnabled)
	{
		if (lazyLoadingEnabled)
		{
			try
			{
				Resources.classForName("net.sf.cglib.proxy.Enhancer");
			}
			catch (Throwable e)
			{
				throw new IllegalStateException(
						"Cannot enable lazy loading because CGLIB is not available. Add CGLIB to your classpath.", e);
			}
		}
		this.lazyLoadingEnabled = lazyLoadingEnabled;
	}

	public boolean isAggressiveLazyLoading()
	{
		return aggressiveLazyLoading;
	}

	public void setAggressiveLazyLoading(boolean aggressiveLazyLoading)
	{
		this.aggressiveLazyLoading = aggressiveLazyLoading;
	}

	public boolean isMultipleResultSetsEnabled()
	{
		return multipleResultSetsEnabled;
	}

	public void setMultipleResultSetsEnabled(boolean multipleResultSetsEnabled)
	{
		this.multipleResultSetsEnabled = multipleResultSetsEnabled;
	}

	public boolean isUseGeneratedKeys()
	{
		return useGeneratedKeys;
	}

	public void setUseGeneratedKeys(boolean useGeneratedKeys)
	{
		this.useGeneratedKeys = useGeneratedKeys;
	}

	public ExecutorType getDefaultExecutorType()
	{
		return defaultExecutorType;
	}

	public void setDefaultExecutorType(ExecutorType defaultExecutorType)
	{
		this.defaultExecutorType = defaultExecutorType;
	}

	public boolean isCacheEnabled()
	{
		return cacheEnabled;
	}

	public void setCacheEnabled(boolean cacheEnabled)
	{
		this.cacheEnabled = cacheEnabled;
	}

	public Integer getDefaultStatementTimeout()
	{
		return defaultStatementTimeout;
	}

	public void setDefaultStatementTimeout(Integer defaultStatementTimeout)
	{
		this.defaultStatementTimeout = defaultStatementTimeout;
	}

	public boolean isUseColumnLabel()
	{
		return useColumnLabel;
	}

	public void setUseColumnLabel(boolean useColumnLabel)
	{
		this.useColumnLabel = useColumnLabel;
	}

	public Properties getVariables()
	{
		return variables;
	}

	public void setVariables(Properties variables)
	{
		this.variables = variables;
	}

	public TypeHandlerRegistry getTypeHandlerRegistry()
	{
		return typeHandlerRegistry;
	}

	public TypeAliasRegistry getTypeAliasRegistry()
	{
		return typeAliasRegistry;
	}

	public ObjectFactory getObjectFactory()
	{
		return objectFactory;
	}

	public void setObjectFactory(ObjectFactory objectFactory)
	{
		this.objectFactory = objectFactory;
	}

	public ObjectWrapperFactory getObjectWrapperFactory()
	{
		return objectWrapperFactory;
	}

	public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory)
	{
		this.objectWrapperFactory = objectWrapperFactory;
	}

	public MetaObject newMetaObject(Object object)
	{
		return MetaObject.forObject(object, objectFactory, objectWrapperFactory);
	}

	public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject,
			BoundSql boundSql)
	{
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
		parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler);
		return parameterHandler;
	}

	public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds,
			ParameterHandler parameterHandler, ResultHandler resultHandler, BoundSql boundSql)
	{
		ResultSetHandler resultSetHandler = mappedStatement.hasNestedResultMaps()
				? new NestedResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql,
						rowBounds)
				: new FastResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql,
						rowBounds);
		resultSetHandler = (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
		return resultSetHandler;
	}

	public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement,
			Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler)
	{
		StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject,
				rowBounds, resultHandler);
		statementHandler = (StatementHandler) interceptorChain.pluginAll(statementHandler);
		return statementHandler;
	}

	public Executor newExecutor(Transaction transaction)
	{
		return newExecutor(transaction, defaultExecutorType);
	}

	public Executor newExecutor(Transaction transaction, ExecutorType executorType)
	{
		executorType = executorType == null ? defaultExecutorType : executorType;
		executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
		Executor executor;
		if (ExecutorType.BATCH == executorType)
		{
			executor = new BatchExecutor(this, transaction);
		}
		else if (ExecutorType.REUSE == executorType)
		{
			executor = new ReuseExecutor(this, transaction);
		}
		else
		{
			executor = new SimpleExecutor(this, transaction);
		}
		if (cacheEnabled)
		{
			executor = new CachingExecutor(executor);
		}
		executor = (Executor) interceptorChain.pluginAll(executor);//所有的拦截器都加入executor里面
		return executor;
	}

	public void addKeyGenerator(String id, KeyGenerator keyGenerator)
	{
		keyGenerators.put(id, keyGenerator);
	}

	public Collection<String> getKeyGeneratorNames()
	{
		return keyGenerators.keySet();
	}

	public Collection<KeyGenerator> getKeyGenerators()
	{
		return keyGenerators.values();
	}

	public KeyGenerator getKeyGenerator(String id)
	{
		return keyGenerators.get(id);
	}

	public boolean hasKeyGenerator(String id)
	{
		return keyGenerators.containsKey(id);
	}

	public void addCache(Cache cache)
	{
		caches.put(cache.getId(), cache);
	}

	public Collection<String> getCacheNames()
	{
		return caches.keySet();
	}

	public Collection<Cache> getCaches()
	{
		return caches.values();
	}

	public Cache getCache(String id)
	{
		return caches.get(id);
	}

	public boolean hasCache(String id)
	{
		return caches.containsKey(id);
	}

	public void addResultMap(ResultMap rm)
	{
		resultMaps.put(rm.getId(), rm);
		checkLocallyForDiscriminatedNestedResultMaps(rm);
		checkGloballyForDiscriminatedNestedResultMaps(rm);
	}

	public Collection<String> getResultMapNames()
	{
		return resultMaps.keySet();
	}

	public Collection<ResultMap> getResultMaps()
	{
		return resultMaps.values();
	}

	public ResultMap getResultMap(String id)
	{
		return resultMaps.get(id);
	}

	public boolean hasResultMap(String id)
	{
		return resultMaps.containsKey(id);
	}

	public void addParameterMap(ParameterMap pm)
	{
		parameterMaps.put(pm.getId(), pm);
	}

	public Collection<String> getParameterMapNames()
	{
		return parameterMaps.keySet();
	}

	public Collection<ParameterMap> getParameterMaps()
	{
		return parameterMaps.values();
	}

	public ParameterMap getParameterMap(String id)
	{
		return parameterMaps.get(id);
	}

	public boolean hasParameterMap(String id)
	{
		return parameterMaps.containsKey(id);
	}

	public void addMappedStatement(MappedStatement ms)
	{
		mappedStatements.put(ms.getId(), ms);
	}

	public Collection<String> getMappedStatementNames()
	{
		buildAllStatements();
		return mappedStatements.keySet();
	}

	public Collection<MappedStatement> getMappedStatements()
	{
		buildAllStatements();
		return mappedStatements.values();
	}

	public Collection<XMLStatementBuilder> getIncompleteStatements()
	{
		return incompleteStatements;
	}

	public void addIncompleteStatement(XMLStatementBuilder incompleteStatement)
	{
		incompleteStatements.add(incompleteStatement);
	}

	public Collection<CacheRefResolver> getIncompleteCacheRefs()
	{
		return incompleteCacheRefs;
	}

	public void addIncompleteCacheRef(CacheRefResolver incompleteCacheRef)
	{
		incompleteCacheRefs.add(incompleteCacheRef);
	}

	public MappedStatement getMappedStatement(String id)
	{
		return this.getMappedStatement(id, true);
	}

	public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements)
	{
		if (validateIncompleteStatements)
		{
			buildAllStatements();
		}
		return mappedStatements.get(id);
	}

	public Map<String, XNode> getSqlFragments()
	{
		return sqlFragments;
	}

	//加入拦截器链
	public void addInterceptor(Interceptor interceptor)
	{
		interceptorChain.addInterceptor(interceptor);
	}

	@SuppressWarnings(
	{ "unchecked" })
	public void addMappers(String packageName, Class superType)
	{
		ResolverUtil<Class> resolverUtil = new ResolverUtil<Class>();
		resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
		Set<Class<? extends Class>> mapperSet = resolverUtil.getClasses();
		for (Class mapperClass : mapperSet)
		{
			addMapper(mapperClass);
		}
	}

	public void addMappers(String packageName)
	{
		addMappers(packageName, Object.class);
	}

	public <T> void addMapper(Class<T> type)
	{
		mapperRegistry.addMapper(type);
	}

	public <T> T getMapper(Class<T> type, SqlSession sqlSession)
	{
		return mapperRegistry.getMapper(type, sqlSession);
	}

	public boolean hasMapper(Class type)
	{
		return mapperRegistry.hasMapper(type);
	}

	public boolean hasStatement(String statementName)
	{
		buildAllStatements();
		return mappedStatements.containsKey(statementName);
	}

	public void addCacheRef(String namespace, String referencedNamespace)
	{
		cacheRefMap.put(namespace, referencedNamespace);
	}

	/**
	 * Parses all the unprocessed statement nodes in the cache. It is recommended
	 * to call this method once all the mappers are added as it provides fail-fast
	 * statement validation.
	 */
	protected void buildAllStatements()
	{
		if (!incompleteCacheRefs.isEmpty())
		{
			synchronized (incompleteCacheRefs)
			{
				// This always throws a BuilderException.
				incompleteCacheRefs.iterator().next().resolveCacheRef();
			}
		}
		if (!incompleteStatements.isEmpty())
		{
			synchronized (incompleteStatements)
			{
				// This always throws a BuilderException.
				incompleteStatements.iterator().next().parseStatementNode();
			}
		}
	}

	/**
	 * Extracts namespace from fully qualified statement id.
	 * 
	 * @param statementId
	 * @return namespace or null when id does not contain period.
	 */
	protected String extractNamespace(String statementId)
	{
		int lastPeriod = statementId.lastIndexOf('.');
		return lastPeriod > 0 ? statementId.substring(0, lastPeriod) : null;
	}

	// Slow but a one time cost. A better solution is welcome.
	protected void checkGloballyForDiscriminatedNestedResultMaps(ResultMap rm)
	{
		if (rm.hasNestedResultMaps())
		{
			for (Map.Entry entry : resultMaps.entrySet())
			{
				Object value = entry.getValue();
				if (value instanceof ResultMap)
				{
					ResultMap entryResultMap = (ResultMap) value;
					if (!entryResultMap.hasNestedResultMaps() && entryResultMap.getDiscriminator() != null)
					{
						Collection<String> discriminatedResultMapNames = entryResultMap.getDiscriminator()
								.getDiscriminatorMap().values();
						if (discriminatedResultMapNames.contains(rm.getId()))
						{
							entryResultMap.forceNestedResultMaps();
						}
					}
				}
			}
		}
	}

	// Slow but a one time cost. A better solution is welcome.
	protected void checkLocallyForDiscriminatedNestedResultMaps(ResultMap rm)
	{
		if (!rm.hasNestedResultMaps() && rm.getDiscriminator() != null)
		{
			for (Map.Entry entry : rm.getDiscriminator().getDiscriminatorMap().entrySet())
			{
				String discriminatedResultMapName = (String) entry.getValue();
				if (hasResultMap(discriminatedResultMapName))
				{
					ResultMap discriminatedResultMap = resultMaps.get(discriminatedResultMapName);
					if (discriminatedResultMap.hasNestedResultMaps())
					{
						rm.forceNestedResultMaps();
						break;
					}
				}
			}
		}
	}

	protected static class StrictMap<J extends String, K extends Object> extends HashMap<J, K>
	{

		private String name;

		public StrictMap(String name, int initialCapacity, float loadFactor)
		{
			super(initialCapacity, loadFactor);
			this.name = name;
		}

		public StrictMap(String name, int initialCapacity)
		{
			super(initialCapacity);
			this.name = name;
		}

		public StrictMap(String name)
		{
			super();
			this.name = name;
		}

		public StrictMap(String name, Map<? extends J, ? extends K> m)
		{
			super(m);
			this.name = name;
		}

		public K put(J key, K value)
		{
			if (containsKey(key))
				throw new IllegalArgumentException(name + " already contains value for " + key);
			if (key.contains("."))
			{
				final String shortKey = getShortName(key);
				if (super.get(shortKey) == null)
				{
					super.put((J) shortKey, value);
				}
				else
				{
					super.put((J) shortKey, (K) new Ambiguity(shortKey));
				}
			}
			return super.put(key, value);
		}

		public K get(Object key)
		{
			K value = super.get(key);
			if (value == null)
			{
				throw new IllegalArgumentException(name + " does not contain value for " + key);
			}
			if (value instanceof Ambiguity)
			{
				throw new IllegalArgumentException(((Ambiguity) value).getSubject() + " is ambiguous in " + name
						+ " (try using the full name including the namespace, or rename one of the entries)");
			}
			return value;
		}

		private String getShortName(J key)
		{
			final String[] keyparts = key.split("\\.");
			final String shortKey = keyparts[keyparts.length - 1];
			return shortKey;
		}

		protected static class Ambiguity
		{
			private String subject;

			public Ambiguity(String subject)
			{
				this.subject = subject;
			}

			public String getSubject()
			{
				return subject;
			}
		}
	}

}
