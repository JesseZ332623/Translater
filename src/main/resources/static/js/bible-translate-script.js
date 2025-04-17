/**
 * 在翻译内容文本框的文字超出文本框范围时，
 * 保持滚动条始终在最低位置（维持内容的跟踪）。
*/
function keepTextareaScroll() 
{
    const textarea = document.getElementById('target');

    // 使用 requestAnimationFrame 确保在渲染后执行
    requestAnimationFrame(() => {
        textarea.scrollTop = textarea.scrollHeight;
    });
}


/**
 * 翻译结果的流式输出。
 * 
 * @param {string}  text      输出内容
 * @param {Object}  element   输出目标元素
 * @param {number}  speed     输出延迟（默认是 25 毫秒，数值越小速度越快）
 * 
 * @returns {Promise<Function>} 一个承诺，调用这个函数必须使用 await 关键字等待它执行完毕。
*/
function streamOutput(text, element, speed = 25) 
{
    let i               = 0;
    let cursorVisible   = true;
    element.value       = '';

    const cursorInterval = setInterval(() => {
        cursorVisible = !cursorVisible;
        updateDisplay();
    }, 500);

    const updateDisplay = () => {
        const currentText = text.substring(0, i);
        element.value = currentText + (cursorVisible ? '$' : '');
    };

    return new Promise(resolve => {
        const timer = setInterval(() => {
            if (i < text.length) {
                i++;
                updateDisplay();
                keepTextareaScroll();

            } else {
                clearInterval(timer);
                clearInterval(cursorInterval);
                element.value = text;
                resolve();
            }
        }, speed);
    });
}

/**
 * 翻译成功的处理。
 * 
 * @param {Array}  data   翻译结果数据（一个 Array）
 * @param {Object} button 页面中翻译按钮的 DOM 
 */
async function handleTranslationSuccess(data, button) 
{
    // 获取翻译输出文本框的 DOM
    const targetDOM = document.getElementById('target');

    // 获取翻译输出文本框下状态栏的 DOM
    const statusDOM = document.getElementById('output-status');

    try 
    {
        /** 
         * if 语句表达式内的 ?. 为可选链操作符（ES2020 标准加入）。
         * 
         * 才此处表示的意思是：
         *      假如 data.translation 为 null 或 undefind，
         *      那么整个表达式（data.translation?.length）就直接返回 undefind，
         *      表达式 undefind > 0 的值必为 false。
         *      
         *      反之，data.translation 不为 null 或 undefind，
         *      那么则继续访问 length 属性。
        */
        if (data.translation?.length > 0) 
        {
            statusDOM.textContent = 'TRANSLATING...';

            // 等待流式输出完毕
            await streamOutput(data.translation[0], targetDOM, 15);

            // 恢复按钮的文字内容并令其可用
            document.getElementById('translate-button').textContent = 'Translate';
            button.disabled = false;

            /**
             * 在翻译输出文本框下状态栏中输出完成的状态，示例：
             *      [2025-04-17T01:42:56] DONE (1254 chars)
            */
            statusDOM.textContent
                = `[${new Date().toISOString().slice(0, 19)}] DONE (${data.translation[0].length} chars)`;
        }
        else 
        {
            throw new Error('No translation available....');
        }
    }
    catch (error) 
    {
        statusDOM.textContent = 'ERROR';
        await streamOutput(`Error: ${error.message}`, targetDOM, 15);
    }
}

/**
 * 读取选择框的 value，作为语言的代码。
 * 
 * @param {string} id 元素 id
*/
function handleLanguageChange(id) { 
    return document.getElementById(id).value; 
}

/**
 * 将翻译记录存于数据库。
 * 
 * @param {Object} recordData 
 */
async function storeTranslateRecord(recordData) 
{
    const recordDataJson = JSON.stringify(recordData);
    console.info(recordDataJson);

    try {
        const response = await fetch(
            'http://localhost:8091/api/translate_record/add_new_record',
            {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: recordDataJson
            }
        );

        console.log(await response.text());
    }
    catch (error) {
        console.error("API Error:", error);
    }
}

/**
 * 通过用户名去获得用户对应的 APP ID 和 APP Key
 *（该函数是和返回结果皆为异步，所以只有别的异步函数才能用 await 关键字修饰的情况下调用）。
 * 
 * @param {string} name 接口用户名
 * 
 * @returns {Promise<>} 封装了请求结果 JSON 的承诺
*/
async function getApplicationKeyByName(name) 
{
    try 
    {
        const response
            = await fetch(`http://localhost:8091/api/interface_info/search/${name}`);

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error('API Error: ', error);
    }
}

/**
 * 调用网易翻译的 API 接口，执行翻译操作，并存储翻译记录。
 * 
 * @param {Object} button 执行翻译的按钮对象
 */
async function doTranslate(button) 
{
    try 
    {
        // 获取 Jesse 的 APP ID 和 APP Key
        const resultAppKeyJson = await getApplicationKeyByName('Jesse');

        // APP 的 ID 和密码不得直接存为明文（应该从数据库取出）
        const config = 
        {
            appKey  : resultAppKeyJson.appKey,
            key     : resultAppKeyJson.secretKey,
            query   : document.getElementById('source').value,
            from    : handleLanguageChange('source_select'),
            to      : handleLanguageChange('target_select')
        };

        const salt     = Date.now();
        const curtime  = Math.floor(Date.now() / 1000);
        const signStr  = config.appKey + truncate(config.query) + salt + curtime + config.key;
        const sign     = CryptoJS.SHA256(signStr).toString(CryptoJS.enc.Hex);

        document.getElementById('translate-button').textContent = 'Translating...';
        button.disabled = true;

        $.ajax({
            url: 'https://openapi.youdao.com/api',
            type: 'post',
            dataType: 'jsonp',
            timeout: 3500,
            data: {
                q           : config.query,
                appKey      : config.appKey,
                salt        : salt,
                from        : config.from,
                to          : config.to,
                sign        : sign,
                signType    : "v3",
                curtime     : curtime
            },
            success: (data) => {

                // 构建记录数据
                const record = 
                {
                    queryTime           : new Date().toISOString().slice(0, 19),
                    sourceLanguageCode  : config.from,
                    targetLanguageCode  : config.to,
                    sourceContent       : config.query.substring(0, 20) + (config.query.length > 50 ? '...' : ''),
                    targetContent       : data.translation?.[0]?.substring(0, 20) + '...' || '',
                    responseCode        : data.errorCode
                };

                storeTranslateRecord(record);
                handleTranslationSuccess(data, button);
            },
            error: (jqXHR, textStatus, errorThrown) => {

                // 处理网络错误、超时、HTTP 状态码错误等
                console.error('AJAX Error:', textStatus, errorThrown);
                let errorMessage = '翻译失败，请重试';
                let errorCode = null;

                // 尝试从响应中提取错误码（如 API 返回的 errorCode）
                if (jqXHR.responseText) 
                {
                    try 
                    {
                        const errorData = JSON.parse(jqXHR.responseText);
                        errorCode = errorData.errorCode || errorData.error;
                        errorMessage = `错误码 ${errorCode}: ${errorData.error || '未知错误'}`;
                    } catch (e) {
                        console.error(e);
                    }
                }

                // 处理超时
                if (textStatus === 'timeout') {
                    errorMessage = '请求超时，请检查网络';
                }

                // 记录失败信息
                const errorRecord = 
                {
                    queryTime           : new Date().toISOString().slice(0, 19),
                    sourceLanguageCode  : config.from,
                    targetLanguageCode  : config.to,
                    sourceContent       : config.query.substring(0, 20) + (config.query.length > 50 ? '...' : ''),
                    targetContent       : errorMessage,
                    responseCode        : errorCode // 存储错误码
                };

                storeTranslateRecord(record);

                alert('翻译失败！');
                button.disabled = false;
            }
        });
    }
    catch (error) {
        console.error(error.message);
    }
}

// 字符串截断处理
function truncate(query) 
{
    return query.length <= 20 ? query
        : query.substring(0, 10) + query.length + query.slice(-10);
}