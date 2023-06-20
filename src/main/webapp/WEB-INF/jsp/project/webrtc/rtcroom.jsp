<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chat Room</title>
    <!-- Latest minified Bootstrap & JQuery-->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <!-- Custom styles for this template -->
    <style>
        .btn.active {
            display: none;
        }

        .btn span:nth-of-type(1)  {
            display: none;
        }
        .btn span:last-child  {
            display: block;
        }

        .btn.active span:nth-of-type(1)  {
            display: block;
        }
        .btn.active span:last-child  {
            display: none;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="/css/rtc/main.css"/>
</head>
<body class="text-center">

<!-- Begin page content -->
<main role="main" class="container-fluid">
    <h1>ChatForYOU with WebRTC</h1>
    <input type="hidden" id="id" name="id" th:value="${room.roomId}"/>
    <input type="hidden" id="uuid" name="uuid" th:value="${uuid}">
    <div class="col-lg-12 mb-3">
        <div class="mb-3" th:text=" '@ Room #' + ${room.roomName}">
            Local User Id
        </div>
        <div class="col-lg-12 mb-3">
            <div class="d-flex justify-content-around mb-3">
                <div id="buttons" class="row">
                    <div class="btn-group mr-2" role="group">
                        <div class="mr-2" data-toggle="buttons">
                            <label class="btn btn-outline-success" id="video_off">
                                <input type="radio" name="options" style="display:none" autocomplete="off">Video On
                            </label>
                            <label class="btn btn-outline-warning active" id="video_on">
                                <input type="radio" name="options" style="display:none" autocomplete="off" checked>Video Off
                            </label>
                        </div>
                        <div class="mr-2" data-toggle="buttons">
                            <label class="btn btn-outline-success" id="audio_off">
                                <input type="radio" name="options" style="display:none" autocomplete="off">Audio On
                            </label>
                            <label class="btn btn-outline-warning active" id="audio_on">
                                <input type="radio" name="options" style="display:none" autocomplete="off" checked>Audio Off
                            </label>
                        </div>
                    </div>

                    <a href="/">
                        <button type="button" class="btn btn-outline-danger" id="exit" name="exit">
                            Exit Room
                        </button>
                    </a>
                </div>
            </div>
        </div>

        <div class="row justify-content-around mb-3">
            <div class="col-lg-6 mb-3">
                <video id="local_video" autoplay playsinline></video>
            </div>
            <div class="col-lg-6 mb-3">
                <video id="remote_video" autoplay playsinline></video>
            </div>
        </div>
    </div>
</main>

<script src="/js/rtc/webrtc_client.js"></script>
</body>
</html>