/* salesTrainCreateProblem.js */

var ST = ST || {};

jQuery(function ($) {

    "use strict";

    ST.CreateProblem = {
        init: function () {
            // Global Variables
            this.problem;
            this.problemClass = '';
            this.problemId = '';
            this.problemDetails;
            this.render();
        },

        render: function () {
            this.cacheElements();
            this.bindEvents();
            $('a[href="#createProblem"]').hide();
        },

        cacheElements: function () {
            //Element bindings
            this.mainContainer = $('.main-container');
            this.anotherMainContainer = $('.another-main-container');

            //Templates

            this.problemDetailsTemplate = Handlebars.compile($('#problem-details-template').html());
            this.adminProblemDetailsTemplate = Handlebars.compile($('#admin-problem-details-template').html());
            this.outputTemplate = Handlebars.compile($('#output-template').html());
        },

        bindEvents: function () {
            this.mainContainer
                .on('hover', '.problem-solution-box', $.proxy(this.displaySolutionBox, this))
                .on('blur keyup', '#className', $.proxy(this.freezeClassName, this))
                .on('click', '#code-edit-class', $.proxy(this.editClassName, this))
                .on('click', '.code-execute', $.proxy(this.executeCode, this))
                .on('cut paste keyup', '#problemCodeStart, #problemCodeEnd', $.proxy(this.disableSubmit, this))
                .on('click', '.code-submit', $.proxy(this.submitCode, this));


            this.anotherMainContainer
                .on('click', '.problem-div', $.proxy(this.fetchProblemDetails, this))
                .on('click', '.code-execute', $.proxy(this.executeCode, this))
                .on('cut paste keyup', '#problemCode', $.proxy(this.disableSubmitInView, this))
                .on('click', '.code-submit', $.proxy(this.submitCode, this));

            $('body')
                .on('click', 'a[href="#viewProblems"]', $.proxy(this.displaySidebar, this))
                .on('click', 'a[href="#createProblem"]', $.proxy(this.clearForm, this))
                .on('click', '.login', $.proxy(this.logout, this))
                .on('click', '#view-probs', $.proxy(function () {$('a[href="#viewProblems"]').click(); $('#successModal').modal('toggle');}, this))
                .on('click', '#create-prob', $.proxy(function () {$('a[href="#createProblem"]').click(); $('#successModal').modal('toggle');}, this))
                .on('click', '.submit-feedback', $.proxy(this.submitFeedback, this))
                .on('click', '.view-feedback', $.proxy(this.viewFeedback, this))
                .on('click', '#save-feedback', $.proxy(this.saveFeedback, this))
                .on('change', '#userName', $.proxy(this.registerLogin, this));
        },

        // This method is used to display the name of the audience in the preview overlay, for which the user rankings are currently being shown
        displaySolutionBox: function (e) {
            console.log('Hello')
        },

        submitFeedback: function (e) {
            var that = this;

            $('#feedback').val('').prop('disabled', false);
            $('input.star')
                .rating('select', -1)
                .rating('enable');

            $('#save-feedback').show();

            $('.star').rating({
                  focus: function(value, link){
                    var tip = $('#hover-test');
                    tip[0].data = tip[0].data || tip.html();
                    tip.html(link.title || 'value: '+value);
                  },
                  blur: function(value, link){
                    var tip = $('#hover-test');
                    $('#hover-test').html(tip[0].data || '');
                  }
                });

            $.each(that.problemDetails.answers, function (key, answer) {
                if (answer.feedback) {
                    if ($(e.target).attr('data-answer-id') === answer.problemId) {
                        $('#feedback').val(answer.feedback);
                        $('input.star').rating('select', +answer.rating - 1);
                    }
                }
            });

            $('#save-feedback').attr('data-answer-id', $(e.target).attr('data-answer-id'));
        },

        viewFeedback: function (e) {
            var that = this;

            $('#feedback').val('').prop('disabled', false);
            $('input.star')
                .rating('select', -1)
                .rating('enable');

            $('.star').rating({
                  focus: function(value, link){
                    var tip = $('#hover-test');
                    tip[0].data = tip[0].data || tip.html();
                    tip.html(link.title || 'value: '+value);
                  },
                  blur: function(value, link){
                    var tip = $('#hover-test');
                    $('#hover-test').html(tip[0].data || '');
                  }
                });

            $.each(that.problemDetails.answers, function (key, answer) {
                if (answer.rating && answer.rating > 0 && answer.rating <= 5 && answer.answeredBy === $('#loggedInUser').text().trim()) {
                    $('#feedback').val(answer.feedback).prop('disabled', true);
                    $('input.star')
                        .rating('select', +answer.rating - 1)
                        .rating('disable');

                }
            });

            $('#save-feedback').hide();
        },

        saveFeedback: function (e) {
            var that = this,
                url = '/training/file/rateAnswer',
                update = {
                    'rating': ($('.star-rating-on').length),
                    'questionId': $(e.target).attr('data-answer-id'),
                    'feedback': $('#feedback').val()
                };

            that.postData(url, update)
            .complete(function (response) {
                $('.currently-selected-div').click();
            });
        },

        disableSubmit: function () {
            $('.code-execute').prop('disabled', $('#problemCodeStart').val().trim().length === 0 && $('#problemCodeEnd').val().trim().length === 0 ? true : false);
            $('.code-submit').prop('disabled', true);
        },

        disableSubmitInView: function () {
            $('.code-submit').prop('disabled', true);
        },

        registerLogin: function (e) {
            $('#loggedInUser').text($(e.target).val());

            if($(e.target).val().indexOf('(') !== -1) {
                $('.login').text('Login');
            } else {
                if($(e.target).val().length !== 0) {
                    $('.login').text('Logout (' + $(e.target).val() + ')');
                } else {
                    $('.login').text('Login');
                }
            }

            $('a[href="#createProblem"]')[$(e.target).val() === 'admin' ? 'show' : 'hide']();
            $('.close-modal').click();

            $('.problem-worksheet').addClass('hidden');
        },

        clearForm: function (e) {
            var that = this;
            $('#problemCode').prop('disabled', false);
            $('#problemDescription, #problemTitle, #className, #problemCodeStart, #problemCodeEnd').val('');
            $('.problem-class-name').text('');
            $('.problem-container, .problem-control-panel').addClass('hidden');

            if (!$('#code-edit-class').hasClass('hidden')) {
                $('#code-edit-class').trigger('click');
            }
        },

        logout: function (e) {
            if($('.login').text().indexOf('(') !== -1) {
                $('.login').text('Login');
                $('#userName option:first').prop('selected', true);
                $('.problem-worksheet').addClass('hidden');
                $('#loggedInUser').text('');
            }

            $('a[href="#createProblem"]').hide();
            e.preventDefault();
        },

        freezeClassName: function (e) {
            var that = this,
                enteredClassName = $('#className').val();

            if(e.type === 'keyup' && e.which == 13 || e.type === 'focusout') {
                if (enteredClassName.trim().length !== 0) {
                    $('.problem-class-name')
                        .removeClass('hidden').text($('#className').val())
                        .parent()
                        .find('#className').hide()
                        .parent()
                        .find('#code-edit-class').removeClass('hidden');

                    that.generateStaticCode();
                    $('.problem-container, .problem-control-panel').removeClass('hidden');
                    $('.code-submit').prop('disabled', true);
                    $('.code-execute').prop('disabled', $('#problemCodeStart').val().trim().length === 0 && $('#problemCodeEnd').val().trim().length === 0 ? true : false);
                }
            }
        },

        editClassName: function (e) {
            var that = this;

            e.preventDefault();

            $('.problem-class-name')
                .addClass('hidden')
                .parent()
                .find('#className').show()
                .parent()
                .find('#code-edit-class').addClass('hidden');
        },

        generateStaticCode: function () {
            $('.problem-class-name-start').text('public class ' + $('.problem-class-name').text().trim() + ' {');
            $('.problem-class-name-end').text('}');
        },

        fetchProblemDetails: function (e) {
            var that = this,
                problemId,
                isDisabled = false,
                problemDetails,
                loggedInUser = $('#loggedInUser').text().trim();

            $('.problem-div').removeClass('currently-selected-div');

            if (!$(e.target).hasClass('problem-div')) {
                problemId = $(e.target).closest('.problem-div').addClass('currently-selected-div').attr('data-problem-id');
            } else {
                problemId = $(e.target).addClass('currently-selected-div').attr('data-problem-id');
            }

            if (loggedInUser.length === 0) {
                return;
            }

            that.getData('/training/file/fetchProblemCode?problemId=' + problemId)
            .complete(function (response) {
                problemDetails = JSON.parse(response.responseText);
                that.problemDetails = problemDetails;
                that.problemClass = problemDetails.problem.className;
                that.problemId = problemDetails.problem.problemId;

                if (loggedInUser === 'admin') {
                    $.each(problemDetails.problem.codeLines, function (key, codeline) {
                        problemDetails.problem.codeLines[key] = codeline.concat('\n');
                    });

                    $.each(problemDetails.answers, function (key1, answer) {
                        $.each(answer.codeLines, function (key2, codeline) {
                            problemDetails.answers[key1].codeLines[key2] = codeline.concat('\n');
                        });
                    });

                    $('.problem-worksheet').removeClass('hidden').html(that.adminProblemDetailsTemplate(problemDetails));
                } else {
                    $.each(problemDetails.answers, function (key1, answer) {
                        $.each(answer.codeLines, function (key2, codeline) {
                            problemDetails.answers[key1].codeLines[key2] = codeline.concat('\n');
                        });
                    });

                    $.each(problemDetails.problem.codeLines, function (key, codeline) {
                        problemDetails.problem.codeLines[key] = codeline.concat('\n');
                    });

                    $.each(problemDetails.answers, function (key, answer) {
                        if (answer.answeredBy === loggedInUser) {
                            problemDetails.problem.isDisabled = true;
                            problemDetails.problem.codeLines = answer.codeLines;

                            if (answer.rating && answer.rating > 0 && answer.rating <= 5) {
                                problemDetails.problem.isFeedbackAvailable = true;
                            } else {
                                problemDetails.problem.isFeedbackAvailable = false;
                            }
                        }
                    });

                    $('.problem-worksheet')
                        .removeClass('hidden').html(that.problemDetailsTemplate(problemDetails))
                        .find('.code-submit').prop('disabled', true);
                }
            });
        },

        executeCode: function (e) {
            var that = this,
                importCode = [],
                coreCode = [],
                responseArray = [],
                code = [],
                dotPos,
                loggedInUser = $('#loggedInUser').text().trim(),
                problemClass;

            e.preventDefault();
            $('#outputModal .output-space').text('');
            that.problem = {};

            if (that.problemClass.length !== 0) {
                dotPos = that.problemClass.indexOf('.');
                problemClass = that.problemClass.substr(0, dotPos);
            }


            if($('li.active a[href="#viewProblems"]').length !== 0) {
                if (loggedInUser === 'admin') {
                    code = $(e.target).parent().prev().find('#problemCode').val().split('\n');

                    that.problem = {
                            'className' : problemClass,
                            'language':'Java',
                            'code': code,
                    };
                } else {
                    code = $('#problemCode').val().split('\n');

                    that.problem = {
                            'problemDescription' : $('.display-problem-description').val().trim(),
                            'problemTitle' : $('.display-problem-title').val().trim(),
                            'className' : problemClass,
                            'language':'Java',
                            'code': code,
                            'adminId': 0,
                            'adminUserName': '',
                            'questionId': that.problemId,
                            'participants': [],
                            'answeredBy': $('#loggedInUser').text()
                    };
                }
            } else {
                code = code.concat($('#problemCodeStart').val().split('\n'));
                code.push($('.problem-class-name-start').text());
                code = code.concat($('#problemCodeEnd').val().split('\n'));
                code.push($('.problem-class-name-end').text());

                that.problem = {
                        'problemDescription' : $('#problemDescription').val().trim(),
                        'problemTitle' : $('#problemTitle').val().trim(),
                        'className' : $('.problem-class-name').text().trim(),
                        'language':'Java',
                        'code': code,
                        'adminId': 1,
                        'adminUserName': 'admin',
                        'questionId':'',
                        'participants': ['akshay_oommen@apple.com', 'cpahuja@apple.com', 'arun_mohan@apple.com']
                };
            }

            that.postData('/training/file/compileCode', that.problem)
            .complete(function (response) {
                responseArray = JSON.parse(response.responseText);

                if ($('#problemCode').prop('disabled') === true) {
                    $('.code-submit').prop('disabled', true);
                }else{
                    $('.code-submit').prop('disabled', !responseArray.flag);

                }

                $('.output-space').html(that.outputTemplate(responseArray.compiledOutput));
            });
        },

        submitCode: function (e) {
            var that = this;

            e.preventDefault();

            that.postData('/training/file/createProblem', that.problem)
            .complete(function (response) {
                if ($('a[href="#viewProblems"]').parent().hasClass('active')) {
                    $('.currently-selected-div').click();
                }
            });
        },

        displaySidebar: function (e) {
            var that = this,
                url = '/training/file/admin/fetchProblemsByLanguage?lang=Java';

            this.problemsTable = $('#problemsTable').dataTable({
                'sAjaxSource' : url,
                'sAjaxDataProp' : 'problems',
                'sScrollY': '600px',
                'bFilter' : false,
                'bDestroy': true,
                'bPaginate': false,
                'bInfo': false,
                'bLengthChange': false,
                'iDisplayLength': 25,
                'oLanguage': {
                    'sZeroRecords': 'No problems available'
                },
                'aoColumns': [{
                    'mData': 'problemId',
                    'bSortable': false,
                    'mRender': function (data, type, full) {
                        return formatCell(full);
                    }
                }],
                'fnServerData': function (sSource, aoData, fnCallback) {
                    var ajaxRequest = that.getData(url, {});

                    ajaxRequest
                        .done(function (json) {
                            fnCallback(json);
                            console.log('success');
                        })
                        .fail(function (jqXHR, textStatus, errorThrown) {
                            console.log('fail')
                        })
                        .always(function (jqXHR) {
                            console.log('complete');
                        });
                },
            });

            function formatCell(full) {
                return '<div class="problem-div" title="' + full.metaData.title + '" data-problem-id="' + full.problemId  + '"><p class="sidebar-title">' + full.metaData.title + '</p><p class="sidebar-desc">' + full.metaData.description + '</p></div>';
            }
        },

        postData : function (url, data, encodeResponse) {
            var headerMap = {
                    'Content-Type' : 'application/json',
                    'Accept' : 'application/json'
                }

            console.log('Post Data:' + JSON.stringify(data) + ' Url: ' + url);
            return $.ajax({
                url : url,
                type : 'POST',
                data : JSON.stringify(data),
                headers : headerMap
            });
        },
        getData : function (url, encodeResponse, params) {
            var headerMap = {
                    'Content-Type' : 'application/json',
                    'Accept' : 'application/json'
                }

            return $.ajax({
                url : url,
                type : 'GET',
                data : {},
                headers : headerMap
            });
        },
    };

    ST.CreateProblem.init();

    // Handlebar helpers
//    Handlebars.registerHelper('geo_list', function (context, code) {
//        var ret = '';
//        for (var i = 0; i < context.length; i++) {
//            if (context[i].geoCode === code) {
//                ret += '<option value="' + context[i].geoCode + '" selected="selected">' + context[i].geoName + '</option>';
//            } else {
//                ret += '<option value="' + context[i].geoCode + '">' + context[i].geoName + '</option>';
//            }
//        }
//        return new Handlebars.SafeString(ret);
//    });
});
