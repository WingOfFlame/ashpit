function esf14(userid, htmlId) {
    "use strict";
    var key = "2f0d72e2-ba37-40ac-b0f4-e3d4e7352b2b";
    var wsURL = "https://" + server;

    var templates = {};
   
    var model = {
        data: {"term":"1149","projectedLevel":"3A","courses":[{"subjectCode":"CO","catalog":"250","id":0,"eventArray":[],"completeArray":[],"s":[],"exam_info":{"section":"001","day":"Tuesday","date":"2014-12-09","start_time":"7:30 PM","end_time":"10:00 PM","location":"PAC 7,8","notes":""},"section":"001"},{"subjectCode":"CS","catalog":"251","id":1,"eventArray":[],"completeArray":[],"s":[],"exam_info":{"section":"001","day":"Monday","date":"2014-12-15","start_time":"4:00 PM","end_time":"6:30 PM","location":"PAC 7,8","notes":""},"section":"001"},{"subjectCode":"CS","catalog":"341","id":2,"eventArray":[],"completeArray":[],"s":[],"exam_info":{"section":"002","day":"Wednesday","date":"2014-12-17","start_time":"9:00 AM","end_time":"11:30 AM","location":"PAC 7,8","notes":""},"section":"002"},{"subjectCode":"CS","catalog":"348","id":3,"eventArray":[{"name":"Review Slides","date":"2014-12-3","priority":"3","notes":"Finish all slides","eid":"C3_E0","color":"list-group-item list-group-item-danger","icon":"glyphicon-time"}],"completeArray":[{"name":"Start Review","date":"2014-12-2","cdate":"2014/12/2","priority":"2","notes":"","eid":"C3_CE0","color":"list-group-item list-group-item-success","icon":"glyphicon-ok"}],"s":[],"exam_info":{"section":"002","day":"Monday","date":"2014-12-08","start_time":"9:00 AM","end_time":"11:30 AM","location":"RCH 301,302,305,306,307","notes":""},"section":"002"},{"subjectCode":"CS","catalog":"349","id":4,"eventArray":[],"completeArray":[],"s":[],"exam_info":{"section":"002","day":"Thursday","date":"2014-12-11","start_time":"9:00 AM","end_time":"11:30 AM","location":"RCH 301,302","notes":""},"section":"002"},{"subjectCode":"MATH","catalog":"237","id":5,"eventArray":[],"completeArray":[],"s":[],"exam_info":{"section":"004","day":"Thursday","date":"2014-12-11","start_time":"12:30 PM","end_time":"3:00 PM","location":"PAC 4,5,6,11,12","notes":""},"section":"004"}]},
        apikey_justin: "d491e0bb128b7386e4975f618b62543f",
        current_term: {},
        bio: {},
        history: {},
        views: [],
        getData: function () {
            var that = this;
            $.getJSON(wsURL + "/getUserData/storeJson/" + userid + "?key=" + key,
                function (d) {
                    if (d.meta.status === "200 OK") {
                        that.data = d.result;
                        var i = 0;
                        for (i = 0; i < that.data.courses.length; i++) {
                            (function (i) {
                                that.data.courses[i].exam_info.count_down = function () {
                                    return function (val, render) {
                                        var from = render(val);
                                        var cd = countDown(from);
                                        if (cd < 0) {
                                            cd = "";
                                        } else if (cd === 0) {
                                            cd = "Due";
                                        }
                                        return cd;
                                    };
                                };
                            })(i);
                        }
                        //console.log(JSON.stringify(that.data));
                    } else {
                        console.log("Failed to get user data." + JSON.stringify(d.meta));
                    }
                });
        },

        storeData: function () {
            //console.log("storeJson: storing " + this.userData);
            var that = this;
            $.ajax({
                type: "POST",
                url: wsURL + "/storeUserData/storeJson/" + userid + "?key=" + key,
                datatype: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(that.data),
                success: function (data) {
                    // Only get the data if we successfully stored it
                    that.getData();
                },
                failure: function (msg) {
                    $(htmlId).html("<h1>FAILED to store user data</h1><p>" + msg + "</p>");
                }
            });
        },

        clearData: function () {
            var that = this;
            that.data = {};
            //console.log(JSON.stringify(that.data));
            that.storeData();
        },

        // Initialize this object
        init: function () {
            //console.log("initializing model");
            var that = this;
            var local = false;

            // Initialize bio
            $.getJSON("https://" + server + "/api/v1/student/stdBio/" + userid,
                function (d) {
                    //console.log(JSON.stringify(d));
                    if (d.meta.status === "200 OK") {
                        that.bio = d.result;
                        that.updateViews("bio");
                    } else {
                        that.updateViews("bio_error");
                    }
                }).fail(function (jqxhr, textStatus, error) {
                var err = textStatus + ", " + error;
                console.log("Request Failed: " + err);
            });


            // Initialize term
            //console.log("gettng current_term info");
            var term_call = $.when($.getJSON(wsURL + "/getUserData/storeJson/" + userid + "?key=" + key,
                    function (d) {
                        if (d.meta.status === "200 OK") {
                            that.data = d.result;
                            //console.log(JSON.stringify(that.data));
                        } else {
                            console.log("Failed to get user data." + JSON.stringify(d.meta));
                        }
                    }), $.getJSON("https://api.uwaterloo.ca/v2/terms/list.json?key=" + model.apikey_justin,
                    function (d) {
                        if (d.meta.status === 200) {
                            that.current_term = d.data.current_term;
                        }
                    }).fail(function (jqxhr, textStatus, error) {
                    var err = textStatus + ", " + error;
                    console.log("Request Failed: " + err);
                }),
                $.getJSON("https://" + server + "/api/v1/student/stdCourseDetails/" + userid,
                    function (d) {
                        that.history = d.result;

                    }).fail(function (jqxhr, textStatus, error) {
                    var err = textStatus + ", " + error;
                    console.log("Request Failed: " + err);
                })).done(function () {
                if ($.isEmptyObject(that.data) || that.current_term != that.data.term) {

                    var temp = that.history.terms;
                    //console.log("a = " + JSON.stringify(a));
                    temp = $.grep(temp, function (n) {
                        return n.term == that.current_term;
                    });
                    //console.log("a = " + JSON.stringify(a));
                    that.data = temp[0];
                    //console.log("coursesView.updateView with c = " + JSON.stringify(that.history));
                    var i = 0;
                    for (i = 0; i < that.data.courses.length; i++) {
                        that.data.courses[i].id = i;
                        that.data.courses[i].eventArray = [];
                        that.data.courses[i].completeArray = [];
                        that.data.courses[i].s = [];
                        that.data.courses[i].exam_info = {};
                        that.data.courses[i].section = that.data.courses[i].sections[0].sectionCode;
                        delete that.data.courses[i].transferCourse;
                        delete that.data.courses[i].courseShortTitle;
                        delete that.data.courses[i].sections;
                    }
                    //console.log(JSON.stringify(that.data.functions.count_down));
                }
                that.updateViews("courses");
            }).done(function () {
                var i = 0;
                for (i = 0; i < that.data.courses.length; i++) {
                    (function (i) {
                        var subject = that.data.courses[i].subjectCode;
                        var catalog = that.data.courses[i].catalog;
                        if(that.data.courses[i].section === '081'){
                            that.data.courses[i].section ='081 Online';
                        }
                        var section = that.data.courses[i].section;
                        $.getJSON("https://api.uwaterloo.ca/v2/courses/" + subject + "/" + catalog + "/examschedule.json?key=" + model.apikey_justin,
                            function (d) {
                                //console.log(d);
                                if (d.meta.status === 200) {
                                    var all_sections = d.data.sections;
                                    all_sections = $.grep(all_sections, function (n) {
                                        return n.section == section;
                                    });
                                    that.data.courses[i].exam_info = all_sections[0];
                                    if(that.data.courses[i].section ==='081 Online'){
                                        that.data.courses[i].exam_info.date = yesterdayFormat().replace(/-/g, "/");
                                    }
                                    that.data.courses[i].exam_info.count_down = function () {
                                        return function (val, render) {
                                            var from = render(val);
                                            var cd = countDown(from);
                                            if (cd < 0) {
                                                cd = " ";
                                            } else if (cd === 0) {
                                                cd = "Due";
                                            }
                                            return cd;
                                        };
                                    };
                                }
                            }).fail(function (jqxhr, textStatus, error) {
                            var err = textStatus + ", " + error;
                            console.log("Request Failed: " + err);
                        }).done(function () {
                            that.updateViews("courses");

                        });
                    })(i);
                }
            });

        },
        /**model.history.courses.length
         
         * Add a new view to be notified when the model changes.
         */
        addView: function (view) {
            this.views.push(view);
            view("");
        },
        /**
         * Update all of the views that are observing us.
         */
        updateViews: function (msg) {
            //console.log("updateViews " + msg);
            var i = 0;
            for (i = 0; i < this.views.length; i++) {
                this.views[i](msg);
            }
        }
    };


    var bioView = {
        updateView: function (msg) {
            //console.log("bioView.updateView with msg = " + msg + " bio = " + JSON.stringify(model.bio));
            if (msg === "bio") {
                var name = Mustache.render(templates.bio, model.bio);
                $("#esf14_bio").html(name);
            } else if (msg === "bio_error") {
                $("#esf14_bio").html("Error loading web service data");
            }
        },
        // Initialize this object
        init: function () {
            //console.log("initializing bio view");
        }
    };

    var coursesView = {
        updateView: function (msg) {
            //console.log("coursesView.updateView with c = " + JSON.stringify(model.data));
            if (msg === "courses") {
                var t = Mustache.render(templates.courses, model.data);
                $("#esf14_courseList").html(t);
                var i = 0;
                //console.log(model.data.courses[i]);
                for (i = 0; i < model.data.courses.length; i++) {
                    //console.log(i);
                    $("#esf14_Course_" + i.toString()).click({
                        value: i
                    }, function (event) {
                        $(htmlId).html(templates.eventlistView);
                        eventlistView.initView(event.data.value);
                    });
                }
            }
            //console.log(JSON.stringify(model.data));
        },
        // Initialize this object
        init: function () {
            //console.log("initializing coursesView");
        }
    };

    /*var MainView = {
     updateView: function(msg) {
     },
     initView: function() {
     }
     };*/




    //=============================================================================================================================================
    //=============================================================================================================================================
    //=============================================================================================================================================
    var eventlistView = {
        updateView: function (courseIndex) {},
        initView: function (courseIndex) {
            //console.log("Initializing eventlistView with courseIndex " + courseIndex);
            //console.log(curCourse);
            var curCourse = model.data.courses[courseIndex].subjectCode + " " + model.data.courses[courseIndex].catalog;
            $("#esf14_modalTitle").text(curCourse);

            var curArray = model.data.courses[courseIndex].eventArray;
            var curcomArray = model.data.courses[courseIndex].completeArray;

            var t = Mustache.render(templates.eventlistView, model.data.courses[courseIndex]);
            $("#esf14_eventlistView").html(t);

            var i = 0;
            //console.log(model.data.courses[i]);
            for (i = 0; i < curArray.length; i++) {
                //console.log("#esf14_C" + courseIndex + "_E" + i.toString());
                $("#esf14_C" + courseIndex + "_E" + i.toString()).click({
                    value: i,
                    value2: courseIndex
                }, function (event) {
                    editEeventView.initView("edit", event.data.value2, event.data.value);
                });
            }

            for (i = 0; i < curcomArray.length; i++) {
                $("#esf14_C" + courseIndex + "_CE" + i.toString()).click({
                    value: i,
                    value2: courseIndex
                }, function (event) {
                    completedEventView.initView(event.data.value2, event.data.value);
                });
            }

            $("#esf14_GoBack").click(function () {
                //console.log("esf14_GoBack clicked");
                try {
                    $(htmlId).html(templates.mainView);
                    //MainView.initView();
                    model.updateViews("bio");
                    model.updateViews("courses");
                } catch (err) {
                    console.log("caught error: " + err);
                }
            });

            $("#esf14_AddEvent").click({
                value: courseIndex,
                value2: curArray.length
            }, function (event) {
                try {
                    //console.log("Passing " + event.data.value);
                    addEventView.initView(event.data.value, event.data.value2);
                } catch (err) {
                    console.log("caught error: " + err);
                }
            });
            //model.addView(eventlistView.updateView);
        }

    };

    function toggle(bool) {
        $("#esf14_eventMonth").prop("disabled", bool);
        $("#esf14_eventYear").prop("disabled", bool);
        $("#esf14_eventDay").prop("disabled", bool);
        if (bool) {
            $("input[type=radio]").prop('disabled', bool);
        } else {
            $("input[type=radio]").removeAttr('disabled');
        }

        $("#esf14_title").prop('disabled', bool);
        $("#esf14_eventNotes").prop('disabled', bool);
        if (bool) {
            $("#esf14_eventHighPrioritybtn").prop('class', "btn btn-default disabled");
            $("#esf14_eventMediumPrioritybtn").prop('class', "btn btn-default disabled");
            $("#esf14_eventLowPrioritybtn").prop('class', "btn btn-default disabled");
        } else {
            $("#esf14_eventHighPrioritybtn").prop('class', "btn btn-default");
            $("#esf14_eventMediumPrioritybtn").prop('class', "btn btn-default active");
            $("#esf14_eventLowPrioritybtn").prop('class', "btn btn-default");
        }
    }
    function obtainForm(mode, courseIndex, eventIndex) {
        //console.log("Initializing obtainForm with courseIndex " + courseIndex);
        var curCourse = model.data.courses[courseIndex].subjectCode + " " + model.data.courses[courseIndex].catalog;
        var curArray = model.data.courses[courseIndex].eventArray;
        var curcomArray = model.data.courses[courseIndex].completeArray;
        var em = document.getElementById("esf14_eventMonth");
        var ed = document.getElementById("esf14_eventDay");
        var ey = new Date().getFullYear();
        //console.log(ey+"-"+em+"-"+ed);
        //name
        var n = $("#esf14_title").val();
        //console.log(n);
        var date = "2014-" + em.options[em.selectedIndex].value + "-" + ed.options[ed.selectedIndex].value;
        var d = date.replace(/-/g, "/");
        //console.log("choosen date: " + d);
        //default priority
        var p = $("#esf14_eventMediumPriority").val();
        //notes
        var no = $("#esf14_eventNotes").val();
        //default colourscheme
        var c = "list-group-item list-group-item-warning";
        //icon
        if (d < todayFormat()) {
            var ic = "glyphicon-remove";
        } else {
            var ic = "glyphicon-time";
        }

        // change colorscheme and priority
        if (mode === "complete") {
            c = "list-group-item list-group-item-success";
            ic = "glyphicon-ok";
        } else if ($("#esf14_eventHighPriority").is(':checked')) {
            p = $("#esf14_eventHighPriority").val();
            c = "list-group-item list-group-item-danger";
        } else if ($("#esf14_eventLowPriority").is(':checked')) {
            p = $("#esf14_eventLowPriority").val();
            c = "list-group-item list-group-item-info";
        }
        //console.log("new priority: " + p);


        if (mode === "add") {
            //console.log("add id " + id);
            var esf14_event = {
                name: n,
                date: date,
                priority: p,
                notes: no,
                eid: "",
                color: c,
                icon: ic
            };
            curArray.push(esf14_event);
        } else if (mode === "complete") {
            //console.log("Complete");
            var today = todayFormat();
            var esf14_comevent = {
                name: n,
                date: date,
                cdate: today,
                priority: p,
                notes: no,
                eid: "",
                color: c,
                icon: ic
            };
            curcomArray.push(esf14_comevent);
            curArray.splice(eventIndex, 1);
        } else if (mode === "uncomplete") {
            var esf14_event = {
                name: n,
                date: date,
                priority: p,
                notes: no,
                eid: "",
                color: c,
                icon: ic
            };
            curArray.push(esf14_event);
            curcomArray.splice(eventIndex, 1);
        } else if (mode === "edit") {
            var curEvent = curArray[eventIndex];
            curEvent.name = n;
            curEvent.date = date;
            curEvent.priority = p;
            curEvent.notes = no;
            curEvent.color = c;
            curEvent.icon = ic;

        }

    }


    function countDown(examDate) {
        var today = new Date(todayFormat()).getTime();
        var ed = new Date(examDate.replace(/-/g, "/")).getTime();
        var timeDiff = ed - today;
        if (timeDiff < 0) {
            return -1;
        } else {
            return Math.ceil(timeDiff / (1000 * 3600 * 24));
        }
    }

    function todayFormat() {
        var current = new Date();
        var month = current.getMonth() + 1;
        var day = current.getDate();
        var year = current.getFullYear();
        var today = year + "/" + month + "/" + day;
        //console.log(today);
        return today;
    }
    function yesterdayFormat() {
        var current = new Date();
        var month = current.getMonth() + 1;
        var day = current.getDate()-1;
        var year = current.getFullYear();
        var today = year + "/" + month + "/" + day;
        //console.log(today);
        return today;
    }

    function sort(A) {
        var i;
        var key;
        var j;
        for (var i = 1; i < A.length; i++) {
            key = A[i];
            j = i - 1;
            while (j >= 0 && larger(A[j], key)) {
                A[j + 1] = A[j];
                j = j - 1;
            }
            A[j + 1] = key;
        }
    }

    function assignId(A, prefix) {
        //console.log("A's length: " + A.length);
        for (var j = 0; j < A.length; j++) {
            var neweid = prefix + j;
            A[j].eid = neweid;
        }
    }


    function larger(a, b) {
        return (smaller(b, a) && (!(b.date === a.date && b.priority === a.priority && b.name === a.name)));
    }

    function smaller(a, b) {
        //console.log(a);
        //console.log(b);
        if (a.date < b.date) {
            return true;
        } else if (a.date === b.date) {
            if (a.priority > b.priority) {
                return true;
            } else if (a.priority === b.priority) {
                if (a.name < b.name) {
                    return true;
                }
            }
        }
        return false;
    }

    function dayChanger(startday, endday, select) {
        //console.log ("startday: " + startday);
        //console.log ("endday: " + endday);
        var ed = document.getElementById("esf14_eventDay");
        var c;
        for (c = 0; c < ed.length; c++) {
            while (c < ed.length) {
                ed.remove(c);
            }
        }
        for (c = 0; c <= endday - startday; c++) {
            var option = document.createElement("option");
            var d = c + startday;
            if (d < 10) {
                option.text = "0" + d;
            } else {
                option.text = d;
            }
            option.value = d;
            ed.add(option);
        }
        ed.selectedIndex = select;
    }

    function monthChanger(startmonth, endmonth, months, select) {
        //console.log ("startmonth: " + startmonth);
        //console.log ("endmonth: " + endmonth);
        var em = document.getElementById("esf14_eventMonth");
        var c;
        for (c = 0; c < em.length; c++) {
            while (c < em.length) {
                em.remove(c);
            }
        }
        for (c = 0; c <= endmonth - startmonth; c++) {
            var option = document.createElement("option");
            var m = months[c + startmonth - 1];
            option.text = months[c + startmonth - 1];
            var m = c + startmonth;
            if (m < 10) {
                option.value = "0" + m;
            } else {
                option.value = m;
            }
            em.add(option);
        }
        em.selectedIndex = select;
    }


    function changeDate(mode, courseIndex, eventIndex) {
        var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        var months31 = ["1", "3", "5", "7", "8", "10", "12"];
        var months30 = ["4", "6", "9", "11"];
        var exam_info = model.data.courses[courseIndex].exam_info.date;
        var examMonth = exam_info.substring(5, 7);
        var examDay = parseInt(exam_info.substring(8, 10));
        //console.log(examDay);
        //console.log(examMonth);
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1;
        var yy = today.getFullYear();
        var em = document.getElementById("esf14_eventMonth");
        var ed = document.getElementById("esf14_eventDay");
        var ey = new Date().getFullYear();
        var startday = 0;
        var endday = 0;
        var startmonth = mm;
        var endmonth = examMonth;
        var select = 0;

        if (mode === "edit") {
            select = parseInt(model.data.courses[courseIndex].eventArray[eventIndex].date.substring(5, 7) - startmonth);
        } else if (mode === "complete") {
            startmonth = parseInt(model.data.courses[courseIndex].completeArray[eventIndex].date.substring(5, 7));
            endmonth = startmonth;
        } else if (mode === "change") {
            select = em.selectedIndex;
        }
        //console.log(1);
        monthChanger(startmonth, endmonth, months, select);

        var selectedMonth = em.options[em.selectedIndex].value;
        if (mode === "complete") {
            startday = parseInt(model.data.courses[courseIndex].completeArray[eventIndex].date.substring(8, 10));
            //console.log("complete day: " + startday);
            endday = startday;
            dayChanger(startday, endday, 0);
            return;
        }

        //console.log(2);
        if (selectedMonth === examMonth) {
            endday = examDay;
        } else if (months30.indexOf(selectedMonth) !== -1) {
            endday = 30;
        } else if (months31.indexOf(selectedMonth) !== -1) {
            endday = 31;
        } else {
            if (yy % 4 === 0) {
                endday = 29;
            } else {
                endday = 28;
            }
        }
        if (selectedMonth === mm + "") {
            startday = dd;
        } else {
            startday = 1;
        }

        select = 0;
        if (mode === "edit") {
            select = parseInt(model.data.courses[courseIndex].eventArray[eventIndex].date.substring(8, 10) - startday);
        } else if (mode === "change") {
            select = ed.selectedIndex;
            //console.log("date selected: "  + select);
            if (!(select < ed.length && select > 0)) {
                select = 0;
            }
        }
        //console.log(3);
        dayChanger(startday, endday, select);
        //console.log(4);
    }

    var addEventView = {
        updateView: function (msg) {},
        initView: function (courseIndex, eventIndex) {
            //console.log("Initializing addEventView with courseIndex " + courseIndex);

            changeDate("init", courseIndex, eventIndex);

            $("#esf14_eventMonth").bind("change", function (esf14_event) {
                changeDate("change", courseIndex, eventIndex);
                //console.log("clicked");
            });

            var curCourse = model.data.courses[courseIndex].subjectCode + " " + model.data.courses[courseIndex].catalog;
            $("#esf14_modalTitle").text(curCourse);
            //console.log(curCourse);

            var curArray = model.data.courses[courseIndex].eventArray;

            $("#esf14_addEvent").show();
            $("#esf14_cancelEvent").show();

            $("#esf14_completeEvent").hide();
            $("#esf14_deleteEvent").hide();
            $("#esf14_editEvent").hide();

            $("#esf14_uncompleteEvent").hide();
            $("#esf14_saveEvent").hide();
            toggle(false);


            $("#esf14_title").val("");
            $("#esf14_eventNotes").val("");
            $("#esf14_eventMediumPriority").checked = true;

            $("#esf14_addEvent").click(function () {
                var value = $("#esf14_title").val();
                //console.log(value);
                if (value !== "") {
                    //console.log("title is not null");
                    $("#esf14_title").parent().prop('class', "input-group");
                    $('#esf14_eventModal').modal('toggle');
                    $('body').removeClass('modal-open');
                    $('.modal-backdrop').remove();
                    obtainForm("add", courseIndex, eventIndex);
                    sort(curArray);
                    assignId(curArray, "C" + courseIndex + "_E");
                    try {
                        eventlistView.initView(courseIndex);
                    } catch (err) {
                        console.log("caught error: " + err);
                    }
                    model.storeData();
                } else {
                    //console.log("title is null");
                    $("#esf14_title").parent().prop('class', "input-group has-error");
                }
            });

            $("#esf14_cancelEvent").click(function () {
                $("#esf14_title").parent().prop('class', "input-group");

            });
        }
    };


    var completedEventView = {
        updateView: function (msg) {},
        initView: function (courseIndex, eventIndex) {
            //console.log("Initializing completedEventView with courseIndex " + courseIndex);
            changeDate("complete", courseIndex, eventIndex);
            var curCourse = model.data.courses[courseIndex].subjectCode + " " + model.data.courses[courseIndex].catalog;
            //console.log(curCourse);
            $("#esf14_modalTitle").text(curCourse);
            var curArray = model.data.courses[courseIndex].eventArray;
            var curcomArray = model.data.courses[courseIndex].completeArray;

            $("#esf14_deleteEvent").show();
            $("#esf14_cancelEvent").show();
            $("#esf14_uncompleteEvent").show();

            $("#esf14_addEvent").hide();
            $("#esf14_editEvent").hide();
            $("#esf14_saveEvent").hide();
            $("#esf14_completeEvent").hide();

            toggle(true);

            $("#esf14_title").val(curcomArray[eventIndex].name);
            $("#esf14_eventNotes").val(curcomArray[eventIndex].notes);
            if (curcomArray[eventIndex].priority === "3") {
                //console.log("High Priority");
                $("#esf14_eventLowPriority").checked = false;
                $("#esf14_eventMediumPriority").checked = false;
                $("#esf14_eventHighPriority").checked = true;
                $("#esf14_eventHighPrioritybtn").prop('class', "btn btn-info disabled");
            } else if (curcomArray[eventIndex].priority === "2") {
                //console.log("Medium Priority");
                $("#esf14_eventLowPriority").checked = false;
                $("#esf14_eventMediumPriority").checked = true;
                $("#esf14_eventHighPriority").checked = false;
                $("#esf14_eventMediumPrioritybtn").prop('class', "btn btn-info disabled");
            } else {
                //console.log("Low Priority");
                $("#esf14_eventLowPriority").checked = true;
                $("#esf14_eventMediumPriority").checked = false;
                $("#esf14_eventHighPriority").checked = false;
                $("#esf14_eventLowPrioritybtn").prop('class', "btn btn-info disabled");
            }

            $("#esf14_uncompleteEvent").click(function () {
                $('#esf14_eventModal').modal('toggle');
                $('body').removeClass('modal-open');
                $('.modal-backdrop').remove();
                obtainForm("uncomplete", courseIndex, eventIndex);
                sort(curArray);
                sort(curcomArray);
                assignId(curArray, "C" + courseIndex + "_E");
                assignId(curcomArray, "C" + courseIndex + "_CE");
                try {
                    eventlistView.initView(courseIndex);
                } catch (err) {
                    console.log("caught error: " + err);
                }
                model.storeData();
            });

            $("#esf14_deleteEvent").click(function () {
                $('#esf14_eventModal').modal('toggle');
                $('body').removeClass('modal-open');
                $('.modal-backdrop').remove();
                curcomArray.splice(eventIndex, 1);
                sort(curcomArray);
                assignId(curcomArray, "C" + courseIndex + "_CE");
                try {
                    eventlistView.initView(courseIndex);
                } catch (err) {
                    console.log("caught error: " + err);
                }
                model.storeData();
            });

        }

    };

    var editEeventView = {
        updateView: function (msg) {},
        initView: function (mode, courseIndex, eventIndex) {
            //console.log("Initializing editEeventView with courseIndex " + courseIndex);
            changeDate("edit", courseIndex, eventIndex);

            var curCourse = model.data.courses[courseIndex].subjectCode + " " + model.data.courses[courseIndex].catalog;
            //console.log(curCourse);
            $("#esf14_modalTitle").text(curCourse);
            var curArray = model.data.courses[courseIndex].eventArray;
            var curcomArray = model.data.courses[courseIndex].completeArray;
            $("#esf14_completeEvent").show();
            $("#esf14_deleteEvent").show();
            $("#esf14_editEvent").show();
            $("#esf14_cancelEvent").show();

            $("#esf14_addEvent").hide();
            $("#esf14_uncompleteEvent").hide();
            $("#esf14_saveEvent").hide();

            $("#esf14_title").val(curArray[eventIndex].name);
            $("#esf14_eventNotes").val(curArray[eventIndex].notes);
            //console.log(curArray[eventIndex].priority);
            toggle(true);

            if (curArray[eventIndex].priority === "3") {
                $("#esf14_eventHighPrioritybtn").prop('class', "btn btn-info disabled");
            } else if (curArray[eventIndex].priority === "2") {
                $("#esf14_eventMediumPrioritybtn").prop('class', "btn btn-info disabled");
            } else {
                $("#esf14_eventLowPrioritybtn").prop('class', "btn btn-info disabled");
            }

            $("#esf14_saveEvent").click(function () {
                var value = $("#esf14_title").val();
                //console.log(value);
                if (value !== "") {
                    //console.log("title is not null");
                    $("#esf14_title").parent().prop('class', "input-group");
                    $('#esf14_eventModal').modal('toggle');
                    $('body').removeClass('modal-open');
                    $('.modal-backdrop').remove();
                    obtainForm("edit", courseIndex, eventIndex);
                    sort(curArray);
                    assignId(curArray, "C" + courseIndex + "_E");
                    try {
                        eventlistView.initView(courseIndex);
                    } catch (err) {
                        console.log("caught error: " + err);
                    }
                    model.storeData();
                } else {
                    //console.log("title is null");
                    $("#esf14_title").parent().prop('class', "input-group has-error");
                }
            });

            $("#esf14_editEvent").click(function () {
                $("#esf14_editEvent").hide();
                $("#esf14_completeEvent").hide();
                $("#esf14_deleteEvent").hide();
                $("#esf14_saveEvent").show();
                toggle(false);
                if (curArray[eventIndex].priority === "3") {
                    //console.log("High Priority");
                    $("#esf14_eventLowPriority").checked = false;
                    $("#esf14_eventMediumPriority").checked = false;
                    $("#esf14_eventHighPriority").checked = true;
                    $("#esf14_eventHighPrioritybtn").prop('class', "btn btn-default active");
                    $("#esf14_eventMediumPrioritybtn").prop('class', "btn btn-default");
                    $("#esf14_eventLowPrioritybtn").prop('class', "btn btn-default");
                } else if (curArray[eventIndex].priority === "2") {
                    //console.log("Medium Priority");
                    $("#esf14_eventLowPriority").checked = false;
                    $("#esf14_eventMediumPriority").checked = true;
                    $("#esf14_eventHighPriority").checked = false;
                    $("#esf14_eventHighPrioritybtn").prop('class', "btn btn-default");
                    $("#esf14_eventMediumPrioritybtn").prop('class', "btn btn-default active");
                    $("#esf14_eventLowPrioritybtn").prop('class', "btn btn-default");
                } else {
                    //console.log("Low Priority");
                    $("#esf14_eventLowPriority").checked = true;
                    $("#esf14_eventMediumPriority").checked = false;
                    $("#esf14_eventHighPriority").checked = false;
                    $("#esf14_eventHighPrioritybtn").prop('class', "btn btn-default");
                    $("#esf14_eventMediumPrioritybtn").prop('class', "btn btn-default");
                    $("#esf14_eventLowPrioritybtn").prop('class', "btn btn-default active");
                }
                $("#esf14_eventMonth").bind("change", function (esf14_event) {
                    changeDate("change", courseIndex, eventIndex);
                });
            });


            $("#esf14_completeEvent").click(function () {
                if ($("#esf14_title").val() !== "") {
                    $('#esf14_eventModal').modal('toggle');
                    $('body').removeClass('modal-open');
                    $('.modal-backdrop').remove();
                    obtainForm("complete", courseIndex, eventIndex);
                    sort(curArray);
                    sort(curcomArray);
                    assignId(curArray, "C" + courseIndex + "_E");
                    assignId(curcomArray, "C" + courseIndex + "_CE");
                    try {
                        eventlistView.initView(courseIndex);
                    } catch (err) {
                        console.log("caught error: " + err);
                    }
                    model.storeData();
                }
            });

            $("#esf14_deleteEvent").click(function () {
                $('#esf14_eventModal').modal('toggle');
                $('body').removeClass('modal-open');
                $('.modal-backdrop').remove();
                curArray.splice(eventIndex, 1);
                sort(curArray);
                assignId(curArray, "C" + courseIndex + "_E");
                try {
                    eventlistView.initView(courseIndex);
                } catch (err) {
                    console.log("caught error: " + err);
                }
                model.storeData();
            });
        }
    };


 

    //=============================================================================================================================================
    /*
     * Initialize the widget.
     */
    //console.log("Initializing courseDescr(" + userid + ", " + htmlId + ")");
    try {
        portal.loadTemplates("widgets/esf14/templates.txt",
            function (t) {
                templates = t;
                $(htmlId).html(templates.mainView);

//model.clearData();
                model.init();
                bioView.init();
                coursesView.init();
                model.addView(bioView.updateView);
                model.addView(coursesView.updateView);
                model.updateViews("bio");
                model.updateViews("courses");
            });
    } catch (err) {
        console.log("caught error: " + err);
    }
}