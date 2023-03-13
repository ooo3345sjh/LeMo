        function onTerms(){
             $('.terms').show();
             $('.location').hide();
             $('#privacy').hide();
             $('.fourteen').hide();
             $('#marketing').hide();
             document.getElementById('cTerms').className = 'on';
             document.getElementById('cLocation').className = 'off';
             document.getElementById('cPrivacy').className = 'off';
             document.getElementById('cFourteen').className = 'off';
             document.getElementById('cMarketing').className = 'off';
         }

        function onLocation(){
             $('.terms').hide();
             $('.location').show();
             $('#privacy').hide();
             $('.fourteen').hide();
             $('#marketing').hide();
             document.getElementById('cTerms').className = 'off';
             document.getElementById('cLocation').className = 'on';
             document.getElementById('cPrivacy').className = 'off';
             document.getElementById('cFourteen').className = 'off';
             document.getElementById('cMarketing').className = 'off';
        }

        function onPrivacy(){
             $('.terms').hide();
             $('.location').hide();
             $('#privacy').show();
             $('.fourteen').hide();
             $('#marketing').hide();
             document.getElementById('cTerms').className = 'off';
             document.getElementById('cLocation').className = 'off';
             document.getElementById('cPrivacy').className = 'on';
             document.getElementById('cFourteen').className = 'off';
             document.getElementById('cMarketing').className = 'off';
        }

        function onFourteen(){
             $('.terms').hide();
             $('.location').hide();
             $('#privacy').hide();
             $('.fourteen').show();
             $('#marketing').hide();
             document.getElementById('cTerms').className = 'off';
             document.getElementById('cLocation').className = 'off';
             document.getElementById('cPrivacy').className = 'off';
             document.getElementById('cFourteen').className = 'on';
             document.getElementById('cMarketing').className = 'off';
        }

        function onMarketing(){
             $('.terms').hide();
             $('.location').hide();
             $('#privacy').hide();
             $('.fourteen').hide();
             $('#marketing').show();
             document.getElementById('cTerms').className = 'off';
             document.getElementById('cLocation').className = 'off';
             document.getElementById('cPrivacy').className = 'off';
             document.getElementById('cFourteen').className = 'off';
             document.getElementById('cMarketing').className = 'on';
        }