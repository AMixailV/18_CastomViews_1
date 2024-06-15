package ru.mixail_akulov.a18_castomviews_1

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import ru.mixail_akulov.a18_castomviews_1.databinding.PartButtonsBinding

/**
 3.
 Создаем enum класс для видов дейстивий
 */
enum class BottomButtonAction {
    POSITIVE,
    NEGATIVE
}

/**
4.
Определяем тип слушателя действий, который будет принимать действие и ничего не возвращать
 */
typealias OnBottomButtonsActionListener = (BottomButtonAction) -> Unit

/**
    1.
    Класс компонента part_buttons.xml
чтобы его сделать таковым, надо его унаследовать от ConstraintLayout, т.к. макет завернут в него.
    В макете ConstraintLayout заменить на merge и дополнительно прописать тэг ConstraintLayout, который
позволяет просматривать компонент в дизайнере.
    Если не поменять на merge, то будут созданы два уровня иерархии с ConstraintLayout-ами,
один при создании объекта класса, второй вложится в него при создании макета.
    Лучше всего использовать конструктор с последним по списку набором параметров.
    Эти параметры мы пропишем и просто передадим дальше родителю для обработки,
    а сами с ними ничего делать не будем.
    Увидеть параметры можно поместив курсор в скобки ConstraintLayout и нажав ctrl+P.
*/

// последний параметр конструктора - стиль по умолчанию, если он задан, для вьюшек. которым стиль не задан иным образом
class BottomButtonsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.bottomButtonsStyle, // установлен в attr.xml и привязан в themes.xml
    defStyleRes: Int = R.style.MyBottomButtonsStyle // установлен в themes.xml, стиль по умолчанию,если нет атрибута defStyleAttr
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

//    конструктор для создания компонента с использованием стандартного стиля, указанного в теме приложения
//    constructor(context: Context,attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0)
//    конструктор для создания компонента из xml файла и кода
//    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
//    конструктор для создания кормпонента из кода
//    constructor(context: Context) : this(context,null)

    /**
    9.
    Изменение состояния прогресс бара определяем в сетерре, 2 способ наряду с методами
     */
    var isProgressMode: Boolean = false
        set(value) {
            field = value
            if (value) {
                binding.positiveButton.visibility = View.INVISIBLE
                binding.negativeButton.visibility = View.INVISIBLE
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.positiveButton.visibility = View.VISIBLE
                binding.negativeButton.visibility = View.VISIBLE
                binding.progress.visibility = View.GONE
            }
        }

    private val binding: PartButtonsBinding

    /**
    6.
    Определяем слушателя созданного в п. 4 типа, который по умолчанию равен null
     */
    private var listener: OnBottomButtonsActionListener? = null

    /**
     * 2.
     - получаем экземпляр инфлейтера
     - подключаем разметку к компоненту, передаем в inflate():
       < макет,
       < ConstraintLayout, в которому будем подключать макет
       < указываем нужно ли присоединять компоненты из макета к нашей вью
     - инициализируем binding, привязывая (bind(), т.к. инфлейтер уже создан) его к инфлейтеру и
     передавая в него this (ConstraintLayout), компилятор все поймет.
     - задаем логику обработки атрибутов из attrs.xml в методе initializeAttributes(), вкоторый передаем
     все атрибуты и инициализируем их -> 2.1.
     */
    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.part_buttons, this, true)
        binding = PartButtonsBinding.bind(this)
        initializeAttributes(attrs, defStyleAttr, defStyleRes)
        initListeners()
    }

    /**
     2.1
     - если атрибуты null, сразу выходим
     - с помощью obtainStyledAttributes() создаем TypedArray, содержащий значения атрибутов в наборе,
     которые перечислены в переданных attrs для переданного name = BottomButtonsView
     и defStyleAttr, defStyleRes, также содержащиеся в attrs.xml (указаны в конструкторе класса).
     */
    private fun initializeAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BottomButtonsView, defStyleAttr, defStyleRes
        )

        with(binding) {
            val positiveButtonText = typedArray
                .getString(R.styleable.BottomButtonsView_bottomPositiveButtonText)
            setPositiveButtonText(positiveButtonText)

            val negativeButtonText = typedArray
                .getString(R.styleable.BottomButtonsView_bottomNegativeButtonText)
            setNegativeButtonText(negativeButtonText)

            val positiveButtonBgColor = typedArray
                .getColor(R.styleable.BottomButtonsView_bottomPositiveBackgroundColor, Color.BLACK) // Color.BLACK срабатоет, если нет defStyleAttr и defStyleRes
            positiveButton.backgroundTintList = ColorStateList.valueOf(positiveButtonBgColor)

            val negativeButtonBgColor = typedArray
                .getColor(R.styleable.BottomButtonsView_bottomNegativeBackgroundColor, Color.WHITE) // Color.WHITE срабатоет, если нет defStyleAttr и defStyleRes
            negativeButton.backgroundTintList = ColorStateList.valueOf(negativeButtonBgColor)

            isProgressMode = typedArray
                .getBoolean(R.styleable.BottomButtonsView_bottomProgressMode, false)
        }

        typedArray.recycle()
    }

    /**
    7.
    Метод инициализации слушателей. Назначаем кнопкам свое значение enum класса и вызываем метод в init,
     после чего можно назначить слушателей кнопкам в main.activity
     */
    private fun initListeners() {
        binding.positiveButton.setOnClickListener {
            this.listener?.invoke(BottomButtonAction.POSITIVE)
        }
        binding.negativeButton.setOnClickListener {
            this.listener?.invoke(BottomButtonAction.NEGATIVE)
        }
    }

    /**
    5.
    Создаем метод назначения слушателя, в который передаем слушателя действий из п. 4
     и назначаем переданное в него значение определенной в п. 6 переменной
     */
    fun setListener(listener: OnBottomButtonsActionListener?) {
        this.listener = listener
    }

    /**
    9. Методы назначения текста кнопкам, 2 способ наряду с сетером
     */
    fun setPositiveButtonText(text: String?) {
        binding.positiveButton.text = text ?: "Ok"
    }

    fun setNegativeButtonText(text: String?) {
        binding.negativeButton.text = text ?: "Cancel"
    }

    /**
    10.
    Метод сохранения состояния.
     Сосотояние сохраняем через класс SavedState
    12.
      - В superState помещаем ссылку на родителя, чтобы он мог себя восстанвить в случае чего
      - Создаем наш savedState из superState с помощью первого конструктора.
      - и ложим значение,которое кнопке назначено
     */
    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()!!
        val savedState = SavedState(superState)
        savedState.positiveButtonText = binding.positiveButton.text.toString()
        savedState.negativeButtonText = binding.negativeButton.text.toString()
        return savedState
    }

    /**
    10.
    Метод восстановления состояния, в который приходит SavedState,
    - поэтому первой строкой мы и приводим ее к типу SavedState
     - передаем в коструктор родителя save родителя savedState.superState, чтобы родитель восстановил свое состояние
     - и далее восстанавливаем свое состояние
     */
    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        binding.positiveButton.text = savedState.positiveButtonText
        binding.negativeButton.text = savedState.negativeButtonText
    }

    /**
    11.
    Класс сохранения состояния
    BaseSavedState - Базовый класс для производных классов, которые хотят сохранять и
    восстанавливать свое состояние в onSaveInstanceState().
     В SavedState объявляем два конструктора:
     - в первом будем передавать состояние родителя super(superState)
     - второй будет считывать наши сохраненные значения из объекта Parcel
     */
    class SavedState : BaseSavedState {

        /** Негативная кнопка сохраняет свое состояние через frees,
          поэтому здесь сохраняем только позитивную */
        var positiveButtonText: String? = null
        var negativeButtonText: String? = null

        constructor(superState: Parcelable) : super(superState)

        /** Для positiveButtonText считываем из Parcel стрингу
         в случае двух переменных,т.к. тут нет ключей, как в бандле, надо строго соблюдать
         последовательность записи и чтения */
        constructor(parcel: Parcel) : super(parcel) {
            positiveButtonText = parcel.readString()
            negativeButtonText = parcel.readString()
        }

        /** Переопределяем writeToParcel, в котором в Parcel записываем значение positiveButtonText*/
        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(positiveButtonText)
            out.writeString(negativeButtonText)
        }

        companion object {
            /** @JvmField Говорит, что когда код скомпилируется и превратится в java байт код,
             переменная станет статической и должна иметь имя именно CREATOR и получает ссылку на анонимный
             объект, унаследованный от Parcelable.Creator<SavedState>*/
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                /** и переопределяем соответствующие методы
                createFromParcel используется, чтобы создать SavedState из Parcel и
                будет вызывать второй конструктор */
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return Array(size) { null }
                }
            }
        }
    }

}