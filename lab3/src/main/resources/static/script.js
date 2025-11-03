  const dropzone = document.getElementById('dropzone');
  const fileInput = document.getElementById('fileInput');
  const jsonText = document.getElementById('jsonText');
  const dropField = document.getElementById('dropField');
  const validateBtn = document.getElementById('validateBtn');
  const sendBtn = document.getElementById('sendBtn');
  const preview = document.getElementById('preview');
  const errors = document.getElementById('errors');
  const endpoint = document.getElementById('endpoint');

  function showError(msg){ errors.textContent = msg }
  function clearError(){ errors.textContent = '' }

  function pretty(obj){ try { return JSON.stringify(obj, null, 2) } catch(e){ return String(obj) } }

  // drag & drop
  ;['dragenter','dragover'].forEach(ev => dropzone.addEventListener(ev, e => {
    e.preventDefault(); e.stopPropagation(); dropzone.style.borderColor = '#66a3ff';
  }));
  ;['dragleave','drop'].forEach(ev => dropzone.addEventListener(ev, e => {
    e.preventDefault(); e.stopPropagation(); dropzone.style.borderColor = '';
  }));

  dropzone.addEventListener('click', ()=> fileInput.click());
  fileInput.addEventListener('change', ev => {
    const f = ev.target.files && ev.target.files[0];
    if(f) readFile(f);
  });

  dropzone.addEventListener('drop', ev => {
    const f = ev.dataTransfer.files && ev.dataTransfer.files[0];
    if(f) readFile(f);
  });

  function readFile(file){
    const reader = new FileReader();
    reader.onload = () => { jsonText.value = reader.result; preview.textContent = reader.result; clearError(); };
    reader.onerror = (e) => showError('Ошибка чтения файла');
    reader.readAsText(file, 'utf-8');
  }

  function parseJson(text){
    try {
      const obj = JSON.parse(text);
      return {ok:true, obj};
    } catch(e){
      return {ok:false, error: e.message};
    }
  }

  validateBtn.addEventListener('click', ()=>{
    clearError(); preview.textContent = '';
    const parsed = parseJson(jsonText.value);
    if(!parsed.ok) { showError('Invalid JSON: ' + parsed.error); return; }
    const withDrop = parsed.obj;
    preview.textContent = pretty(withDrop);
  });

  sendBtn.addEventListener('click', async ()=>{
    clearError();
    const parsed = parseJson(jsonText.value);
    if(!parsed.ok) { showError('Invalid JSON: ' + parsed.error); return; }

    const payload = parsed.obj;
    preview.textContent = pretty(payload);

    sendBtn.disabled = true;
    try{
      const res = await fetch(endpoint.value || '/import', {
        method: 'POST',
        headers: {'Content-Type':'application/json'},
        body: JSON.stringify(payload)
      });
      if(!res.ok){
        const text = await res.text();
        showError('Server error: ' + res.status + ' — ' + text);
      } else {
        const text = await res.text().catch(()=>null);
        preview.textContent = 'Отправлено успешно' + (text? '\n\n' + text : '');
      }
    } catch(e){
      showError('Network error: ' + e.message);
    } finally { sendBtn.disabled = false; }
  });

  // initial sample
  jsonText.value = '{\n  "table": "users",\n  "columns": [ {"name":"id","type":"int"}, {"name":"email","type":"varchar"} ],\n  "rows": [ {"id":1,"email":"a@a","test":"dropme"}, {"id":2,"email":"b@b","test":"dropme"} ]\n}';
  preview.textContent = jsonText.value;