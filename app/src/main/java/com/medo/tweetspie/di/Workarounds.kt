package com.medo.tweetspie.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStores
import org.koin.Koin
import org.koin.android.architecture.ext.get
import org.koin.android.architecture.ext.getByName
import org.koin.core.bean.Definition
import org.koin.core.parameter.Parameters
import org.koin.dsl.context.Context
import org.koin.dsl.context.emptyParameters
import org.koin.standalone.KoinComponent
import kotlin.reflect.KClass

/**
 * ViewModel DSL Extension
 * Allow to declare a vieModel - be later inject into Activity/Fragment with dedicated injector
 */
inline fun <reified T : ViewModel> Context.viewModelX(
        name: String = "",
        noinline definition: Definition<T>
) {
    val bean = factory(name, definition)
    bean.bind(ViewModel::class)
}

/**
 * Lazy get a viewModel instance
 *
 * @param key - ViewModel Factory key (if have several instances from same ViewModel)
 * @param name - Koin BeanDefinition name (if have several ViewModel definition of the same type)
 */
inline fun <reified T : ViewModel> LifecycleOwner.viewModel(
        key: String? = null,
        name: String? = null
): Lazy<T> {
    return viewModelByClass(false, T::class, key, name, emptyParameters())
}

/**
 * Lazy get a viewModel instance
 *
 * @param fromActivity - create it from Activity (default true)
 * @param clazz - Class of the BeanDefinition to retrieve
 * @param key - ViewModel Factory key (if have several instances from same ViewModel)
 * @param name - Koin BeanDefinition name (if have several ViewModel definition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
fun <T : ViewModel> LifecycleOwner.viewModelByClass(
        fromActivity: Boolean,
        clazz: KClass<T>,
        key: String? = null,
        name: String? = null,
        parameters: Parameters = emptyParameters()
): Lazy<T> {
    return lazy { getViewModelByClass(fromActivity, clazz, key, name, parameters) }
}

/**
 * Get a viewModel instance
 *
 * @param fromActivity - create it from Activity (default false) - not used if on Activity
 * @param clazz - Class of the BeanDefinition to retrieve
 * @param key - ViewModel Factory key (if have several instances from same ViewModel)
 * @param name - Koin BeanDefinition name (if have several ViewModel definition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
fun <T : ViewModel> LifecycleOwner.getViewModelByClass(
        fromActivity: Boolean = false,
        clazz: KClass<T>,
        key: String? = null,
        name: String? = null,
        parameters: Parameters = emptyParameters()
): T {
    KoinFactoryX.apply {
        this.parameters = parameters
        this.name = name
    }
    val viewModelProvider = when {
        this is FragmentActivity -> {
            Koin.logger.log("[ViewModel] get for FragmentActivity @ $this")
            ViewModelProvider(ViewModelStores.of(this), KoinFactoryX)
        }
        this is Fragment -> {
            if (fromActivity) {
                Koin.logger.log("[ViewModel] get for FragmentActivity @ ${this.activity}")
                ViewModelProvider(this.viewModelStore, KoinFactoryX)
            } else {
                Koin.logger.log("[ViewModel] get for Fragment @ $this")
                ViewModelProvider(this.viewModelStore, KoinFactoryX)
            }
        }
        else -> error("Can't get ViewModel on $this - Is not a FragmentActivity nor a Fragment")
    }
    return if (key != null) viewModelProvider.get(
            key,
            clazz.java
    ) else viewModelProvider.get(clazz.java)
}

/**
 * Koin ViewModel factory
 */
object KoinFactoryX : ViewModelProvider.Factory, KoinComponent {

    /**
     * Current Parameters
     */
    internal var parameters: Parameters = { emptyMap() }

    /**
     * Current BeanDefinition name
     */
    internal var name: String? = null

    /**
     * Create instance for ViewModelProvider Factory
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val beanName = name
        return if (beanName != null) {
            getByName(beanName, parameters)
        } else get(modelClass, parameters)
    }
}
